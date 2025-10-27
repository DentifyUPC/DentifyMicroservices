package com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findByName(String name);
}
