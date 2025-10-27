package com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByName(String name);
}
