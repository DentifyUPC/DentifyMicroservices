package com.upc.dentify.clinicmanagementservice.domain.model.aggregates;

import com.upc.dentify.clinicmanagementservice.domain.model.valueobjects.*;
import com.upc.dentify.clinicmanagementservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;


@Entity
public class Clinic extends AuditableAbstractAggregateRoot<Clinic> {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(unique = true, nullable = false)
    private RUC ruc;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String businessName;

    @Embedded
    private Address address;

    @Embedded
    @Column(unique = true, nullable = false)
    private Phone phone;

    @Embedded
    @Column(unique = true, nullable = false)
    private ContactEmail contactEmail;

    @NotBlank
    @Column(nullable = false)
    private String logoUrl;

    @Embedded
    @Column(unique = true, nullable = false)
    private PaypalEmail paypalEmail;

    @Embedded
    @Column(unique = true, nullable = false)
    private PaypalMerchantId payPalMerchantId;

    public Clinic() {}

    public Clinic(Long id) {
        setId(id);
    }
}
