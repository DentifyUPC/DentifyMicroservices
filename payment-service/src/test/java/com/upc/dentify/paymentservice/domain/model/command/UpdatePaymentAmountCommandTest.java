package com.upc.dentify.paymentservice.domain.model.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UpdatePaymentAmountCommandTest {

    @Test
    void constructor_initializesCorrectly() {
        UpdatePaymentAmountCommand cmd = new UpdatePaymentAmountCommand(1L, 50.0);

        assertThat(cmd.id()).isEqualTo(1L);
        assertThat(cmd.amount()).isEqualTo(50.0);
    }
}
