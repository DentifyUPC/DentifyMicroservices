package com.upc.dentify.paymentservice.domain.model.command;

public record UpdatePaymentAmountCommand(
        Long id,
        Double amount
) {
}
