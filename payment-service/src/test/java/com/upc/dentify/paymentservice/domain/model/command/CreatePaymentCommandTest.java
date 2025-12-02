package com.upc.dentify.paymentservice.domain.model.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CreatePaymentCommandTest {

    @Test
    void constructor_initializesFieldsCorrectly() {
        CreatePaymentCommand cmd = new CreatePaymentCommand(10L, 20L, 80.5);

        assertThat(cmd.patientId()).isEqualTo(10L);
        assertThat(cmd.appointmentId()).isEqualTo(20L);
        assertThat(cmd.amount()).isEqualTo(80.5);
    }

    @Test
    void constructor_allowsValidValues() {
        assertThatCode(() -> new CreatePaymentCommand(1L, 1L, 1.0))
                .doesNotThrowAnyException();
    }
}
