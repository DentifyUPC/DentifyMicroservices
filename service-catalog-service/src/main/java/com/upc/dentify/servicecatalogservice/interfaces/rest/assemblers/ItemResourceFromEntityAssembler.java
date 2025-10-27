package com.upc.dentify.servicecatalogservice.interfaces.rest.assemblers;

import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Item;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemResource;

public class ItemResourceFromEntityAssembler {

    public static ItemResource toResourceFromEntity(Item entity) {
        return new ItemResource(entity.getId(), entity.getName(), entity.getUnitType().getName());
    }
}
