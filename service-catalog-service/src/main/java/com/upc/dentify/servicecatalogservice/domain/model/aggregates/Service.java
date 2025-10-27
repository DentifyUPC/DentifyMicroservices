package com.upc.dentify.servicecatalogservice.domain.model.aggregates;

import com.upc.dentify.servicecatalogservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Service extends AuditableAbstractAggregateRoot<Service> {

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    public Service() {}

    public Service(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
