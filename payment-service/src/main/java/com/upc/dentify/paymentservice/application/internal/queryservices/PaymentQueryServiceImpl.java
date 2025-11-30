package com.upc.dentify.paymentservice.application.internal.queryservices;

import com.upc.dentify.paymentservice.domain.model.aggregates.Payment;
import com.upc.dentify.paymentservice.domain.model.queries.GetAllPaymentsQuery;
import com.upc.dentify.paymentservice.domain.model.queries.GetPaymentByAppointmentIdQuery;
import com.upc.dentify.paymentservice.domain.model.queries.GetPaymentByIdQuery;
import com.upc.dentify.paymentservice.domain.model.queries.GetPaymentsByPatientIdQuery;
import com.upc.dentify.paymentservice.domain.services.PaymentQueryService;
import com.upc.dentify.paymentservice.infrastructure.persistence.jpa.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {
    
    private final PaymentRepository paymentRepository;

    public PaymentQueryServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> handle(GetAllPaymentsQuery query) {
        return paymentRepository.findAll();
    }

    @Override
    public Optional<Payment> handle(GetPaymentByIdQuery query) {
        return paymentRepository.findById(query.id());
    }

    @Override
    public Optional<Payment> handle(GetPaymentByAppointmentIdQuery query) {
        return paymentRepository.findByAppointmentId(query.appointmentId());
    }

    @Override
    public List<Payment> handle(GetPaymentsByPatientIdQuery query) {
        return paymentRepository.findAllByPatientId(query.patientId());
    }
}
