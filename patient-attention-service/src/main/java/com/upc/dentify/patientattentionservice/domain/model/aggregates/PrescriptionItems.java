package com.upc.dentify.patientattentionservice.domain.model.aggregates;

import com.upc.dentify.patientattentionservice.domain.model.commands.CreatePrescriptionItemCommand;
import com.upc.dentify.patientattentionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PrescriptionItems extends AuditableAbstractAggregateRoot<PrescriptionItems> {
    @NotBlank
    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    public PrescriptionItems() {}

    public PrescriptionItems(CreatePrescriptionItemCommand command) {
        this.medicationName = command.medicationName();
        this.description = command.description();
        this.prescription = new Prescription(command.prescriptionId());
    }
}
