package com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.SchedulePerClinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchedulePerClinicRepository extends JpaRepository<SchedulePerClinic, Long> {
    Optional<SchedulePerClinic> findByClinicId(Long clinicId);
}
