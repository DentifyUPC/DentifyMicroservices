package com.upc.dentify.servicecatalogservice.application.internal.queryservices;

import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllItemsRequiredByServiceIdQuery;
import com.upc.dentify.servicecatalogservice.domain.services.ItemPerServiceQueryService;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.ItemPerServiceRepository;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemRequiredResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemPerServiceQueryServiceImpl implements ItemPerServiceQueryService {

    private final ItemPerServiceRepository itemPerServiceRepository;

    public ItemPerServiceQueryServiceImpl(ItemPerServiceRepository itemPerServiceRepository) {
        this.itemPerServiceRepository = itemPerServiceRepository;
    }

    @Override
    public List<ItemRequiredResource> handle(GetAllItemsRequiredByServiceIdQuery query) {
        return itemPerServiceRepository.findAllItemsByServiceId(query.serviceId());
    }
}
