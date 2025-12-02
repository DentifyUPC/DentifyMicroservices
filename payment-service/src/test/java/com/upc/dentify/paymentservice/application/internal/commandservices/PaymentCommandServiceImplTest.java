package com.upc.dentify.paymentservice.application.internal.commandservices;

import com.paypal.orders.*;
import com.paypal.orders.PurchaseUnit;
import com.upc.dentify.paymentservice.application.internal.outboundservices.PayPalService;
import com.upc.dentify.paymentservice.domain.model.aggregates.Payment;
import com.upc.dentify.paymentservice.domain.model.command.CreatePaymentCommand;
import com.upc.dentify.paymentservice.domain.model.command.ProcessPaymentCommand;
import com.upc.dentify.paymentservice.domain.model.command.UpdatePaymentAmountCommand;
import com.upc.dentify.paymentservice.domain.model.valueobjects.PaymentState;
import com.upc.dentify.paymentservice.infrastructure.persistence.jpa.repositories.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PaymentCommandServiceImplTest {

    @Mock PaymentRepository paymentRepository;
    @Mock PayPalService payPalService;

    @InjectMocks
    PaymentCommandServiceImpl service;

    CreatePaymentCommand validCommand;

    @BeforeEach
    void setup() {
        validCommand = new CreatePaymentCommand(
                10L,
                20L,
                50.0
        );
    }

    // -------------------------------------------------------------------------
    // CREATE PAYMENT
    // -------------------------------------------------------------------------
    @Test
    void handle_createPayment_success() {
        when(paymentRepository.findByAppointmentId(validCommand.appointmentId()))
                .thenReturn(Optional.empty());

        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> {
            Payment p = inv.getArgument(0);
            p.setId(100L);
            return p;
        });

        var result = service.handle(validCommand);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(100L);
        assertThat(result.get().getState()).isEqualTo(PaymentState.PENDING);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void handle_createPayment_appointmentAlreadyHasPayment_throws() {
        when(paymentRepository.findByAppointmentId(validCommand.appointmentId()))
                .thenReturn(Optional.of(new Payment()));

        assertThatThrownBy(() -> service.handle(validCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Payment already exists");
    }

    // -------------------------------------------------------------------------
    // UPDATE AMOUNT
    // -------------------------------------------------------------------------
    @Test
    void handle_updateAmount_success() {
        var update = new UpdatePaymentAmountCommand(1L, 80.0);

        Payment existing = new Payment(validCommand);
        existing.setId(1L);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var result = service.handle(update);

        assertThat(result).isPresent();
        assertThat(result.get().getAmount()).isEqualTo(80.0);

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void handle_updateAmount_paymentNotFound() {
        var cmd = new UpdatePaymentAmountCommand(999L, 90.0);
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Payment not found");
    }

    @Test
    void handle_updateAmount_invalidAmount() {
        var cmd = new UpdatePaymentAmountCommand(1L, 0.0);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(new Payment()));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("greater than 0");
    }

    @Test
    void handle_updateAmount_repositoryThrows_wrapsInIllegalArgumentException() {
        var cmd = new UpdatePaymentAmountCommand(1L, 20.0);

        Payment existing = new Payment(validCommand);
        existing.setId(1L);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(paymentRepository.save(any(Payment.class)))
                .thenThrow(new RuntimeException("db down"));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("An error occurred");
    }

    // -------------------------------------------------------------------------
    // PROCESS PAYMENT
    // -------------------------------------------------------------------------
    @Test
    void handle_processPayment_success() throws IOException {
        ProcessPaymentCommand cmd = new ProcessPaymentCommand(1L, "ORDER-123");

        Payment existing = new Payment(validCommand);
        existing.setId(1L);
        existing.setAmount(50.0);

        Order order = mock(Order.class);
        PurchaseUnit purchaseUnit = mock(PurchaseUnit.class);
        Capture capture = mock(Capture.class);

        when(capture.id()).thenReturn("CAPTURE-123");

// Simulamos que purchaseUnit.payments().captures() devuelve una lista
        var paymentsMock = mock(Object.class);

// Mockito usa “deep stubs” para simular llamadas anidadas
        PurchaseUnit purchaseUnitDeepMock = mock(PurchaseUnit.class, RETURNS_DEEP_STUBS);

// paymentsMock no es una clase real, solo una cadena de mocks
        when(purchaseUnitDeepMock.payments().captures())
                .thenReturn(List.of(capture));

        when(order.purchaseUnits()).thenReturn(List.of(purchaseUnitDeepMock));
        when(order.status()).thenReturn("COMPLETED");



        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(payPalService.captureOrder("ORDER-123")).thenReturn(order);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.handle(cmd);

        assertThat(result).isPresent();
        assertThat(result.get().getState()).isEqualTo(PaymentState.PAID);
        assertThat(result.get().getPaypalOrderId()).isEqualTo("ORDER-123");
        assertThat(result.get().getPaypalCaptureId()).isEqualTo("CAPTURE-123");
        assertThat(result.get().getPaymentDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void handle_processPayment_paymentNotFound() {
        var cmd = new ProcessPaymentCommand(999L, "ORDER-999");
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Payment not found");
    }

    @Test
    void handle_processPayment_alreadyPaid() {
        Payment existing = new Payment(validCommand);
        existing.setState(PaymentState.PAID);
        existing.setId(1L);

        var cmd = new ProcessPaymentCommand(1L, "ORDER-123");

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already been processed");
    }

    @Test
    void handle_processPayment_invalidAmount() {
        Payment existing = new Payment(validCommand);
        existing.setAmount(0.0);
        existing.setId(1L);

        var cmd = new ProcessPaymentCommand(1L, "ORDER-123");

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("amount must be set");
    }

    @Test
    void handle_processPayment_paypalFails() throws IOException {
        Payment existing = new Payment(validCommand);
        existing.setId(1L);

        var cmd = new ProcessPaymentCommand(1L, "ORDER-123");

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(payPalService.captureOrder("ORDER-123"))
                .thenThrow(new IOException("PayPal error"));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Error processing payment");
    }

}
