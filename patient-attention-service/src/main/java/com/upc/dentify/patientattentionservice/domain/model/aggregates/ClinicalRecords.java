package com.upc.dentify.patientattentionservice.domain.model.aggregates;

import com.upc.dentify.patientattentionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ClinicalRecords extends AuditableAbstractAggregateRoot<ClinicalRecords> {

    @OneToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(optional = false)
    @JoinColumn(name = "anamnesis_id", nullable = false)
    private Anamnesis anamnesis;

    @OneToOne(optional = false)
    @JoinColumn(name = "odontogram_id", nullable = false)
    private Odontogram odontogram;

    public ClinicalRecords() {}

    public ClinicalRecords(Long clinicalRecordId) {
        this.setId(clinicalRecordId);
    }

    public ClinicalRecords(Long patientId, Long anamnesisId, Long odontogramId) {
        this.patient = new Patient(patientId);
        this.anamnesis = new Anamnesis(anamnesisId);
        this.odontogram = new Odontogram(odontogramId);
    }
}
