package com.upc.dentify.paymentservice.interfaces.rest.assemblers;

import com.upc.dentify.paymentservice.domain.model.aggregates.Payment;
import com.upc.dentify.paymentservice.interfaces.rest.dtos.PaymentResource;

public class PaymentResourceFromEntityAssembler {
    public static PaymentResource fromEntityToResource(Payment payment) {
        return new PaymentResource(
                payment.getId(),
                payment.getPaymentDate(),
                payment.getAmount(),
                payment.getPatientId(),
                payment.getState().toString(),
                payment.getAppointmentId(),
                payment.getPaypalOrderId(),
                payment.getPaypalCaptureId(),
                payment.getCreatedAt() != null ? payment.getCreatedAt().toString() : null,
                payment.getUpdatedAt() != null ? payment.getUpdatedAt().toString() : null
        );
    }
}
