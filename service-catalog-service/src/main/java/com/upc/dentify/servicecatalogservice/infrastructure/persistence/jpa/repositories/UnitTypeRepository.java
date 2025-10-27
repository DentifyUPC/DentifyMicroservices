package com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.servicecatalogservice.domain.model.entities.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitTypeRepository extends JpaRepository<UnitType, Long> {
    Optional<UnitType> findByName(String name);
}
