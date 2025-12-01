package com.upc.dentify.paymentservice.domain.model.command;

public record CreatePaymentCommand(
        Long patientId,
        Long appointmentId,
        Double amount
) {
}
