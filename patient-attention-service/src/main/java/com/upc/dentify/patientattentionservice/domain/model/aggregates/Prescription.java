package com.upc.dentify.patientattentionservice.domain.model.aggregates;

import com.upc.dentify.patientattentionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Prescription extends AuditableAbstractAggregateRoot<Prescription> {
    @Column(name = "effects")
    private String effects;

    public Prescription() {}

    public Prescription(Long id) {
        this.setId(id);
    }
}
