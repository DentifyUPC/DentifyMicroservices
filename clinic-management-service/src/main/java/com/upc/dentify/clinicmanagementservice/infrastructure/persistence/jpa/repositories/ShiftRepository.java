package com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.Shift;
import com.upc.dentify.clinicmanagementservice.domain.model.valueobjects.ShiftName;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ShiftResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findAllByClinicId(Long clinicId);
    Boolean existsByNameAndClinicId(ShiftName shiftName, Long clinicId);
    @Query("""
        SELECT new com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ShiftResource(
            s.id,
            s.startTime,
            s.endTime,
            CAST(s.name AS STRING),
            s.clinic.id
        )
        FROM Shift s
        WHERE s.clinic.id = :clinicId
    """)
    List<ShiftResource> findAllShiftResourcesByClinicId(Long clinicId);
}
