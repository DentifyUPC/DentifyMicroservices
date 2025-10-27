package com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.Clinic;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ClinicInformationPreRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    @Query("SELECT new com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ClinicInformationPreRegister(c.id, c.name) FROM Clinic c")
    List<ClinicInformationPreRegister> findAllClinicInformationPreRegister();
}
