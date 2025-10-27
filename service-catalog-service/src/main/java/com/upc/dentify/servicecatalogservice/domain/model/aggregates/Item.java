package com.upc.dentify.servicecatalogservice.domain.model.aggregates;

import com.upc.dentify.servicecatalogservice.domain.model.entities.UnitType;
import com.upc.dentify.servicecatalogservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Item extends AuditableAbstractAggregateRoot<Item> {

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_type_id", nullable = false)
    private UnitType unitType;

    public Item() {}

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, UnitType unitType) {
        this.name = name;
        this.unitType = unitType;
    }

    public Item(Long id) {
        this.setId(id);
    }

    public String getName() {
        return name;
    }

    public UnitType getUnitType() {
        return unitType;
    }
}
