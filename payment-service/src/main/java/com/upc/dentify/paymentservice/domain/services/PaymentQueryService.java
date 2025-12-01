package com.upc.dentify.paymentservice.domain.services;

import com.upc.dentify.paymentservice.domain.model.aggregates.Payment;
import com.upc.dentify.paymentservice.domain.model.queries.GetAllPaymentsQuery;
import com.upc.dentify.paymentservice.domain.model.queries.GetPaymentByAppointmentIdQuery;
import com.upc.dentify.paymentservice.domain.model.queries.GetPaymentByIdQuery;
import com.upc.dentify.paymentservice.domain.model.queries.GetPaymentsByPatientIdQuery;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryService {
    List<Payment> handle(GetAllPaymentsQuery query);
    Optional<Payment> handle(GetPaymentByIdQuery query);
    Optional<Payment> handle(GetPaymentByAppointmentIdQuery query);
    List<Payment> handle(GetPaymentsByPatientIdQuery query);
}
