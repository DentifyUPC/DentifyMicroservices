package com.upc.dentify.servicecatalogservice.domain.model.aggregates;

import com.upc.dentify.servicecatalogservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
public class ItemPerService extends AuditableAbstractAggregateRoot<ItemPerService> {

    @Min(1)
    @Column(nullable = false)
    private Long quantityRequired;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    public ItemPerService() {}

    public ItemPerService(Service service, Item item, Long quantityRequired) {
        this.service = service;
        this.item = item;
        this.quantityRequired = quantityRequired;
    }

    public Item getItem() {
        return item;
    }
}
