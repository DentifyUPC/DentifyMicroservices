package com.upc.dentify.patientattentionservice.domain.model.aggregates;

import com.upc.dentify.patientattentionservice.domain.model.valueobjects.PhoneNumber;
import com.upc.dentify.patientattentionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Anamnesis extends AuditableAbstractAggregateRoot<Anamnesis> {
    @Column(name = "clinical_background")
    private String clinicalBackground;

    @Column(name = "low_blood_pressure", nullable = false)
    private Boolean lowBloodPressure;

    @Column(name = "high_blood_pressure", nullable = false)
    private Boolean highBloodPressure;

    @Column(name = "smoke", nullable = false)
    private Boolean smoke;

    @Column(name = "current_medications")
    private String currentMedications;

    @Embedded
    @Column(name = "emergency_contact")
    private PhoneNumber emergencyContact;

    public Anamnesis() {
        this.lowBloodPressure = false;
        this.highBloodPressure = false;
        this.smoke = false;
    }

    public Anamnesis(Long anamnesisId) {
        this.setId(anamnesisId);
    }
}