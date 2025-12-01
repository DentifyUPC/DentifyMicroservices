package com.upc.dentify.paymentservice.interfaces.rest.dtos;

public record CreatePaymentRequest(
        Long patientId,
        Long appointmentId
) {
}
