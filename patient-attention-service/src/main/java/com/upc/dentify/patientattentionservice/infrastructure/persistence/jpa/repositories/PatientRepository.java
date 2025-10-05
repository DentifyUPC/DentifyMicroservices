package com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAllByClinicId(Long clinicId);
    Optional<Patient> findByUserId(Long userId);
    Boolean existsByUserId(Long userId);
}
