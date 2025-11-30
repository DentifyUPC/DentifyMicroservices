package com.upc.dentify.paymentservice.domain.model.command;

public record ProcessPaymentCommand(
        Long id,
        String paypalOrderId
) {
}
