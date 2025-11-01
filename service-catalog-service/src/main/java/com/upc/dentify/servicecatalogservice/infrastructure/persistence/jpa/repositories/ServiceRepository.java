package com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Service;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ServiceResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findByName(String name);

    @Query("""
       SELECT new com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ServiceResource(
           s.id, s.name
       )
       FROM Service s
    """)
    List<ServiceResource> findAllServicesResources();
}
