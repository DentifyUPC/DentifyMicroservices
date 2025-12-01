package com.upc.dentify.paymentservice.application.internal.commandservices;

import com.paypal.orders.Order;
import com.upc.dentify.paymentservice.application.internal.outboundservices.PayPalService;
import com.upc.dentify.paymentservice.domain.model.aggregates.Payment;
import com.upc.dentify.paymentservice.domain.model.command.CreatePaymentCommand;
import com.upc.dentify.paymentservice.domain.model.command.ProcessPaymentCommand;
import com.upc.dentify.paymentservice.domain.model.command.UpdatePaymentAmountCommand;
import com.upc.dentify.paymentservice.domain.model.valueobjects.PaymentState;
import com.upc.dentify.paymentservice.domain.services.PaymentCommandService;
import com.upc.dentify.paymentservice.infrastructure.persistence.jpa.repositories.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {
    
    private final PaymentRepository paymentRepository;
    private final PayPalService payPalService;

    public PaymentCommandServiceImpl(PaymentRepository paymentRepository, 
                                     PayPalService payPalService) {
        this.paymentRepository = paymentRepository;
        this.payPalService = payPalService;
    }

    @Transactional
    @Override
    public Optional<Payment> handle(CreatePaymentCommand command) {
        // Check if payment already exists for this appointment
        // The appointment validation is skipped because the event is published immediately
        // after the appointment is created in appointment-service, so we can safely assume it exists
        if (paymentRepository.findByAppointmentId(command.appointmentId()).isPresent()) {
            throw new IllegalArgumentException("Payment already exists for this appointment");
        }

        Payment payment = new Payment(command);
        paymentRepository.save(payment);
        return Optional.of(payment);
    }

    @Transactional
    @Override
    public Optional<Payment> handle(UpdatePaymentAmountCommand command) {
        var paymentOptional = paymentRepository.findById(command.id());
        if (paymentOptional.isEmpty()) {
            throw new IllegalArgumentException("Payment not found");
        }

        var payment = paymentOptional.get();
        
        if (command.amount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        payment.setAmount(command.amount());

        try {
            var updatedPayment = paymentRepository.save(payment);
            return Optional.of(updatedPayment);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("An error occurred while updating payment amount: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public Optional<Payment> handle(ProcessPaymentCommand command) {
        var paymentOptional = paymentRepository.findById(command.id());
        if (paymentOptional.isEmpty()) {
            throw new IllegalArgumentException("Payment not found");
        }

        var payment = paymentOptional.get();

        if (payment.getState() == PaymentState.PAID) {
            throw new IllegalArgumentException("Payment has already been processed");
        }

        if (payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Payment amount must be set before processing");
        }

        try {
            Order capturedOrder = payPalService.captureOrder(command.paypalOrderId());
            
            if ("COMPLETED".equals(capturedOrder.status())) {
                payment.setState(PaymentState.PAID);
                payment.setPaymentDate(LocalDate.now());
                payment.setPaypalOrderId(command.paypalOrderId());
                
                if (capturedOrder.purchaseUnits() != null && !capturedOrder.purchaseUnits().isEmpty()) {
                    var captures = capturedOrder.purchaseUnits().get(0).payments().captures();
                    if (captures != null && !captures.isEmpty()) {
                        payment.setPaypalCaptureId(captures.get(0).id());
                    }
                }
                
                var updatedPayment = paymentRepository.save(payment);
                return Optional.of(updatedPayment);
            } else {
                throw new IllegalArgumentException("PayPal order capture failed with status: " + capturedOrder.status());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error processing payment with PayPal: " + e.getMessage());
        }
    }
}
