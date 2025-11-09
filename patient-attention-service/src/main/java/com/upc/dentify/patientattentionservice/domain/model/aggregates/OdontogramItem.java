package com.upc.dentify.patientattentionservice.domain.model.aggregates;

import com.upc.dentify.patientattentionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class OdontogramItem extends AuditableAbstractAggregateRoot<OdontogramItem> {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "teeth_id", nullable = false)
    private Teeth teeth;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tooth_status_id", nullable = false)
    private ToothStatus toothStatus;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "odontogram_id", nullable = false)
    private Odontogram odontogram;

    public OdontogramItem() {}
}
