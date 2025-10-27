package com.upc.dentify.servicecatalogservice.infrastructure.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Item;
import com.upc.dentify.servicecatalogservice.domain.model.aggregates.ItemPerService;
import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Service;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.ItemPerServiceRepository;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.ItemRepository;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.ServiceRepository;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemRequiredResourceFromJson;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ServiceCatalogResourceFromJson;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class ItemPerServiceSeeder {

    private final ServiceRepository serviceRepository;
    private final ItemRepository itemRepository;
    private final ItemPerServiceRepository itemPerServiceRepository;
    private final ObjectMapper objectMapper;

    public ItemPerServiceSeeder(ServiceRepository serviceRepository,
                                ItemRepository itemRepository,
                                ItemPerServiceRepository itemPerServiceRepository,
                                ObjectMapper objectMapper) {
        this.serviceRepository = serviceRepository;
        this.itemRepository = itemRepository;
        this.itemPerServiceRepository = itemPerServiceRepository;
        this.objectMapper = objectMapper;
    }

    public void seed() {
//        if (itemPerServiceRepository.count() > 0) return;

        try (InputStream inputStream = getClass().getResourceAsStream("/seed/items_per_services.json")) {
            List<ServiceCatalogResourceFromJson> servicesItems = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<ServiceCatalogResourceFromJson>>() {}
            );

            for (ServiceCatalogResourceFromJson serviceDto : servicesItems) {
                Service service = serviceRepository.findByName(serviceDto.service())
                        .orElseThrow(() -> new RuntimeException("Service not found: " + serviceDto.service()));

                for (ItemRequiredResourceFromJson itemDto : serviceDto.items()) {
                    Item item = itemRepository.findByName(itemDto.name())
                            .orElseThrow(() -> new RuntimeException("Item not found: " + itemDto.name()));

                    itemPerServiceRepository.findByServiceAndItem(service, item)
                            .orElseGet(() -> itemPerServiceRepository.save(
                                    new ItemPerService(service, item, itemDto.quantity())
                            ));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error seeding ItemPerService", e);
        }
    }
}
