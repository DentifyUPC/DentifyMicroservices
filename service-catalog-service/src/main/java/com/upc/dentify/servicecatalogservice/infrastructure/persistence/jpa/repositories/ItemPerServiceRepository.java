package com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Item;
import com.upc.dentify.servicecatalogservice.domain.model.aggregates.ItemPerService;
import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Service;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemRequiredResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemPerServiceRepository extends JpaRepository<ItemPerService, Long> {
    Optional<ItemPerService> findByServiceAndItem(Service service, Item item);

    @Query("""
       SELECT new com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemRequiredResource(
           i.id, i.name, i.unitType.name, ips.quantityRequired
       )
       FROM ItemPerService ips
       JOIN ips.item i
       WHERE ips.service.id = :serviceId
       """)
    List<ItemRequiredResource> findAllItemsByServiceId(@Param("serviceId") Long serviceId);
}
