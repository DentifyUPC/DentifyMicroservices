package com.upc.dentify.servicecatalogservice.infrastructure.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Item;
import com.upc.dentify.servicecatalogservice.domain.model.entities.UnitType;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.ItemRepository;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.UnitTypeRepository;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemResourceFromJson;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.InputStream;
import java.util.List;

@Component
public class ItemSeeder{

    private final ItemRepository itemRepository;
    private final UnitTypeRepository unitTypeRepository;
    private final ObjectMapper objectMapper;

    public ItemSeeder(ItemRepository itemRepository, UnitTypeRepository unitTypeRepository, ObjectMapper objectMapper) {
        this.itemRepository = itemRepository;
        this.unitTypeRepository = unitTypeRepository;
        this.objectMapper = objectMapper;
    }

//    @Override
//    public void run(String... args) throws Exception {
//        if (itemRepository.count() > 0) return; // avoid duplicates
//
//        InputStream inputStream = getClass().getResourceAsStream("/seed/items.json");
//        List<ItemResourceFromJson> items = objectMapper.readValue(
//                inputStream,
//                new TypeReference<List<ItemResourceFromJson>>() {}
//        );
//
//        for (ItemResourceFromJson dto : items) {
//            UnitType unitType = unitTypeRepository.findByName(dto.unitType())
//                    .orElseThrow(() -> new RuntimeException("UnitType not found: " + dto.unitType()));
//
//            itemRepository.findByName(dto.name())
//                    .orElseGet(() -> itemRepository.save(new Item(dto.name(), unitType)));
//        }
//    }

    public void seed() {
//        if (unitTypeRepository.count() > 0) return;

        try (InputStream inputStream = getClass().getResourceAsStream("/seed/items.json")) {
            List<ItemResourceFromJson> items =
                    objectMapper.readValue(inputStream, new TypeReference<>() {});

        for (ItemResourceFromJson dto : items) {
            UnitType unitType = unitTypeRepository.findByName(dto.unitType())
                    .orElseThrow(() -> new RuntimeException("UnitType not found: " + dto.unitType()));

            itemRepository.findByName(dto.name())
                    .orElseGet(() -> itemRepository.save(new Item(dto.name(), unitType)));
        }

        } catch (Exception e) {
            throw new RuntimeException("Error seeding UnitTypes", e);
        }
    }
}
