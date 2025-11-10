package com.upc.dentify.patientattentionservice.domain.model.aggregates;

import com.upc.dentify.patientattentionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ClinicalRecordEntries extends AuditableAbstractAggregateRoot<ClinicalRecordEntries> {
    @Column(name = "evolution")
    private String evolution;

    @Column(name = "observation")
    private String observation;

    @Column(name = "odontologist_id", nullable = false)
    private Long odontologistId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "clinical_record_id", nullable = false)
    private ClinicalRecords clinicalRecords;

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @OneToOne(optional = false)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    public ClinicalRecordEntries() {}

    public ClinicalRecordEntries(Long odontologistId, Long clinicalRecordId, Long appointmentId, Long prescriptionId) {
        this.odontologistId = odontologistId;
        this.clinicalRecords = new ClinicalRecords(clinicalRecordId);
        this.appointmentId = appointmentId;
        this.prescription = new Prescription(prescriptionId);
    }
}
