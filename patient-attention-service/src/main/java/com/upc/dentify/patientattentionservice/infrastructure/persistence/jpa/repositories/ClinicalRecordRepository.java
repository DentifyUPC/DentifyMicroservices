package com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ClinicalRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicalRecordRepository extends JpaRepository<ClinicalRecords, Long> {
    Optional<ClinicalRecords> findByPatientId(Long patientId);
    Boolean existsByPatientId(Long patientId);
}
