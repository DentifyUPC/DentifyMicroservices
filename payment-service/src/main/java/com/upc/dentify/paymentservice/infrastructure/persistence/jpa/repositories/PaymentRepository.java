package com.upc.dentify.paymentservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.paymentservice.domain.model.aggregates.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByAppointmentId(Long appointmentId);
    List<Payment> findAllByPatientId(Long patientId);
}
