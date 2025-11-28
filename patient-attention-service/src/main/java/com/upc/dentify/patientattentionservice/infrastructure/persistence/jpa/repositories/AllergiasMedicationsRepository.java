package com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.AllergiasMedications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergiasMedicationsRepository extends JpaRepository<AllergiasMedications, Long> {
    List<AllergiasMedications> findByClinicalRecordsId(Long clinicalRecordId);
}
