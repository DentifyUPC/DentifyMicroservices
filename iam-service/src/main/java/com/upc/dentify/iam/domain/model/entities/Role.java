package com.upc.dentify.iam.domain.model.entities;

import com.upc.dentify.iam.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Entity
public class Role extends AuditableModel implements GrantedAuthority{

    @Column(nullable = false, unique = true)
    private String name;

    public Role(){}

    public Role(String name) {
        this.name = name;
    }

    public Role(Long id) {
        setId(id);
    }

    @Override
    public String getAuthority() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }


}
