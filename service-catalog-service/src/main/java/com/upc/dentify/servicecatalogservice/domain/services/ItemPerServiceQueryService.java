package com.upc.dentify.servicecatalogservice.domain.services;

import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllItemsRequiredByServiceIdQuery;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemRequiredResource;

import java.util.List;

public interface ItemPerServiceQueryService {
    List<ItemRequiredResource> handle(GetAllItemsRequiredByServiceIdQuery query);
}
