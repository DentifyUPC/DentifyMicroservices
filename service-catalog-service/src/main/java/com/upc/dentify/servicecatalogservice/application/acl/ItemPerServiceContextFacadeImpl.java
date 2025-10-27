package com.upc.dentify.servicecatalogservice.application.acl;

import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllItemsRequiredByServiceIdQuery;
import com.upc.dentify.servicecatalogservice.domain.services.ItemPerServiceQueryService;
import com.upc.dentify.servicecatalogservice.interfaces.acl.ItemPerServiceContextFacade;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemRequiredResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemPerServiceContextFacadeImpl implements ItemPerServiceContextFacade {
    private final ItemPerServiceQueryService itemPerServiceQueryService;

    public ItemPerServiceContextFacadeImpl(ItemPerServiceQueryService itemPerServiceQueryService) {
        this.itemPerServiceQueryService = itemPerServiceQueryService;
    }

    @Override
    public List<ItemRequiredResource> getItemIdsByServiceId(Long serviceId) {
        var query = new GetAllItemsRequiredByServiceIdQuery(serviceId);
        return itemPerServiceQueryService.handle(query);
    }
}
