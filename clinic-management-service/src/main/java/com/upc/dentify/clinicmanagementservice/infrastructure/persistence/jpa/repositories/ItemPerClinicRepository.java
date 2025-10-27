package com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ItemPerClinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPerClinicRepository extends JpaRepository<ItemPerClinic, Long> {

    @Query("SELECT CASE WHEN COUNT(ipc) > 0 THEN TRUE ELSE FALSE END " +
            "FROM ItemPerClinic ipc " +
            "WHERE ipc.clinic.id = :clinicId AND ipc.itemId = :itemId")
    Boolean existsByClinicIdAndItemId(@Param("clinicId") Long clinicId, @Param("itemId") Long itemId);

    @Query("SELECT ipc FROM ItemPerClinic ipc " +
            "JOIN FETCH ipc.clinic " +
            "WHERE ipc.clinic.id = :clinicId")
    List<ItemPerClinic> findAllByClinicId(@Param("clinicId") Long clinicId);
}
