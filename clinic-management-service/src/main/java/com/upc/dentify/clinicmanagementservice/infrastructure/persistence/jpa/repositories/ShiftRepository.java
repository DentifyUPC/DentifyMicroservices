package com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.Shift;
import com.upc.dentify.clinicmanagementservice.domain.model.valueobjects.ShiftName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findAllByClinicId(Long clinicId);
    Boolean existsByNameAndClinicId(ShiftName shiftName, Long clinicId);
}
