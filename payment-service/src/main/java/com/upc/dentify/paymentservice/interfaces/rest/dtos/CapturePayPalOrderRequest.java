package com.upc.dentify.paymentservice.interfaces.rest.dtos;

public record CapturePayPalOrderRequest(
        Long paymentId,
        String orderId
) {
}
