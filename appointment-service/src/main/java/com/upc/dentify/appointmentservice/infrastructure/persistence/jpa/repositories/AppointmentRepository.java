package com.upc.dentify.appointmentservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.appointmentservice.domain.model.aggregates.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByOdontologistId(Long odontologistId);
    List<Appointment> findAllByPatientId(Long patientId);
}
