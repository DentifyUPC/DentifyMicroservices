package com.upc.dentify.servicecatalogservice.domain.model.entities;

import com.upc.dentify.servicecatalogservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class UnitType extends AuditableAbstractAggregateRoot<UnitType> {

    @Column(unique = true, nullable = false)
    private String name;

    public UnitType() {}

    public UnitType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
