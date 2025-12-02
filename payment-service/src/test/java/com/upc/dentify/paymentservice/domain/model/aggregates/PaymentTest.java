package com.upc.dentify.paymentservice.domain.model.aggregates;

import com.upc.dentify.paymentservice.domain.model.command.CreatePaymentCommand;
import com.upc.dentify.paymentservice.domain.model.valueobjects.PaymentState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PaymentTest {

    @Test
    void constructor_setsInitialPendingState() {
        CreatePaymentCommand cmd = new CreatePaymentCommand(1L, 2L, 50.0);

        Payment payment = new Payment(cmd);

        assertThat(payment.getState()).isEqualTo(PaymentState.PENDING);
        assertThat(payment.getAmount()).isEqualTo(50.0);
        assertThat(payment.getPatientId()).isEqualTo(1L);
        assertThat(payment.getAppointmentId()).isEqualTo(2L);
    }

    @Test
    void constructor_nullCommand_throws() {
        assertThatThrownBy(() -> new Payment(null))
                .isInstanceOf(NullPointerException.class);
    }

}
