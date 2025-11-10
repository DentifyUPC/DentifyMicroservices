package com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ClinicalRecordEntries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalRecordEntryRepository extends JpaRepository<ClinicalRecordEntries, Long> {
    Boolean existsByAppointmentId(Long appointmentId);
    List<ClinicalRecordEntries> findAllByClinicalRecords_Id(Long clinicalRecordId);
}
