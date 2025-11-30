package com.upc.dentify.paymentservice.interfaces.rest.dtos;

import java.time.LocalDate;

public record PaymentResource(
        Long id,
        LocalDate paymentDate,
        Double amount,
        Long patientId,
        String state,
        Long appointmentId,
        String paypalOrderId,
        String paypalCaptureId,
        String createdAt,
        String updatedAt
) {
}
