package com.upc.dentify.paymentservice.domain.model.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProcessPaymentCommandTest {

    @Test
    void constructor_initializesCorrectly() {
        ProcessPaymentCommand cmd = new ProcessPaymentCommand(1L, "ORDER-1");

        assertThat(cmd.id()).isEqualTo(1L);
        assertThat(cmd.paypalOrderId()).isEqualTo("ORDER-1");
    }
}
