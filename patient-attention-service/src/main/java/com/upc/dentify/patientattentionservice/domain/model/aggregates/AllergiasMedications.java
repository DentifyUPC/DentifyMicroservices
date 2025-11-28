package com.upc.dentify.patientattentionservice.domain.model.aggregates;

import com.upc.dentify.patientattentionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class AllergiasMedications extends AuditableAbstractAggregateRoot<AllergiasMedications> {
    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "clinical_record_id", nullable = false)
    private ClinicalRecords clinicalRecords;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public AllergiasMedications() {}

    public AllergiasMedications(String medicationName, Long clinicalRecordId) {
        this.medicationName = medicationName;
        this.clinicalRecords = new ClinicalRecords(clinicalRecordId);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
