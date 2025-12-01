package com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ServicesPerClinics;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ServicePerClinicRepository extends JpaRepository<ServicesPerClinics, Long> {
    boolean existsByClinicIdAndServiceId(Long clinicId, Long serviceId);
    List<ServicesPerClinics> findByClinicId(Long clinicId);
    Optional<ServicesPerClinics> findByClinicIdAndServiceId(Long clinicId, Long serviceId);

}
