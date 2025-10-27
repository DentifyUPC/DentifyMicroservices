package com.upc.dentify.servicecatalogservice.interfaces.acl;

import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemRequiredResource;

import java.util.List;

public interface ItemPerServiceContextFacade {
    List<ItemRequiredResource> getItemIdsByServiceId(Long serviceId);
}
