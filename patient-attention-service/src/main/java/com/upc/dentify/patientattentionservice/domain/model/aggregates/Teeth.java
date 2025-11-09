package com.upc.dentify.patientattentionservice.domain.model.aggregates;

import com.upc.dentify.patientattentionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Teeth extends AuditableAbstractAggregateRoot<Teeth> {

    @Column(nullable = false, unique = true)
    private Long code;

    @Column(nullable = false)
    private String name;

    public Teeth() {}

    public Teeth(Long code, String name) {
        this.code = code;
        this.name = name;
    }

}
