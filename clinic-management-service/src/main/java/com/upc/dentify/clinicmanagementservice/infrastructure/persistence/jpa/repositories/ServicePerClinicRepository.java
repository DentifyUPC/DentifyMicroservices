package com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ServicesPerClinics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicePerClinicRepository extends JpaRepository<ServicesPerClinics, Long> {
    boolean existsByClinicIdAndServiceId(Long clinicId, Long serviceId);
}
