package com.upc.dentify.patientattentionservice.domain.model.aggregates;

import com.upc.dentify.patientattentionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;

@Entity
public class Odontogram extends AuditableAbstractAggregateRoot<Odontogram> {

    public Odontogram() {}

    public Odontogram(Long id) {
        this.setId(id);
    }
}
