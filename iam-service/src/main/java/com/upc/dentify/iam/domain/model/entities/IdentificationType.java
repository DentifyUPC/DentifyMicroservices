package com.upc.dentify.iam.domain.model.entities;

import com.upc.dentify.iam.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class IdentificationType extends AuditableModel {

    @Column(nullable = false, unique = true)
    private String name;

    public IdentificationType(){}

    public IdentificationType(String name) {
        this.name = name;
    }

    public IdentificationType(Long id) {
        setId(id);
    }
}
