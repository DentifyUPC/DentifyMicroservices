package com.upc.dentify.servicecatalogservice.application.internal.queryservices;

import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Item;
import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllItemsQuery;
import com.upc.dentify.servicecatalogservice.domain.model.queries.GetItemByIdQuery;
import com.upc.dentify.servicecatalogservice.domain.services.ItemQueryService;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemQueryServiceImpl implements ItemQueryService {

    private final ItemRepository itemRepository;

    public ItemQueryServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> handle(GetAllItemsQuery query) {
        return itemRepository.findAll();
    }

    @Override
    public Optional<Item> handle(GetItemByIdQuery query) {
        return itemRepository.findById(query.id());
    }
}
