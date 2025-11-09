package com.upc.dentify.patientattentionservice.domain.model.aggregates;

import com.upc.dentify.patientattentionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class ToothStatus extends AuditableAbstractAggregateRoot<ToothStatus> {

    @Column(nullable = false, unique = true)
    private String name;

    public ToothStatus() {}

    public ToothStatus(String name) {
        this.name = name;
    }

    public ToothStatus(Long id) {
        this.setId(id);
    }
}
