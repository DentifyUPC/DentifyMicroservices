package com.upc.dentify.clinicmanagementservice.domain.model.aggregates;

import com.upc.dentify.clinicmanagementservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class ServicesPerClinics extends AuditableAbstractAggregateRoot<ServicesPerClinics> {
    @Column(name = "clinic_id", nullable = false)
    private Long clinicId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Column(name = "total_price_per_items")
    private Double totalPricePerItems;

    @Column(name = "total_labor_price")
    private Double totalLaborPrice;

    @Column(name = "total_service_price")
    private Double totalServicePrice;

    public ServicesPerClinics() {}

    public ServicesPerClinics(Long clinicId, Long serviceId,
                            Double totalLaborPrice) {
        this.clinicId = clinicId;
        this.serviceId = serviceId;
        this.totalLaborPrice = totalLaborPrice;
        this.totalServicePrice = 0.0;
        this.totalPricePerItems = 0.0;
    }

    public void calculateTotals(Double totalPricePerItems) {
        this.totalPricePerItems = totalPricePerItems;
        this.totalServicePrice = this.totalPricePerItems + this.totalLaborPrice;
    }

    public void updateTotals(Double totalLaborPrice) {
        this.totalLaborPrice = totalLaborPrice;
        this.totalServicePrice = this.totalLaborPrice + this.totalPricePerItems;
    }
}
