package com.upc.dentify.servicecatalogservice.infrastructure.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.dentify.servicecatalogservice.domain.model.entities.UnitType;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.UnitTypeRepository;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.UnitTypeResourceFromJson;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class UnitTypeSeeder {

    private final UnitTypeRepository unitTypeRepository;
    private final ObjectMapper objectMapper;

    public UnitTypeSeeder(UnitTypeRepository unitTypeRepository, ObjectMapper objectMapper) {
        this.unitTypeRepository = unitTypeRepository;
        this.objectMapper = objectMapper;
    }

//    @Override
//    public void run(String... args) throws Exception {
//        if (unitTypeRepository.count() > 0) return; // avoid duplicates
//
//        // read JSON
//        InputStream inputStream = getClass().getResourceAsStream("/seed/unit_types.json");
//        List<UnitTypeResourceFromJson> unitTypes = objectMapper.readValue(
//                inputStream,
//                new TypeReference<List<UnitTypeResourceFromJson>>() {}
//        );
//
//        // save in database
//        for (UnitTypeResourceFromJson dto : unitTypes) {
//            unitTypeRepository.findByName(dto.name())
//                    .orElseGet(() -> unitTypeRepository.save(new UnitType(dto.name())));
//        }
//    }

    public void seed() {
//        if (unitTypeRepository.count() > 0) return;

        try (InputStream inputStream = getClass().getResourceAsStream("/seed/unit_types.json")) {
            List<UnitTypeResourceFromJson> unitTypes =
                    objectMapper.readValue(inputStream, new TypeReference<>() {});

            for (UnitTypeResourceFromJson dto : unitTypes) {
            unitTypeRepository.findByName(dto.name())
                    .orElseGet(() -> unitTypeRepository.save(new UnitType(dto.name())));
        }
        } catch (Exception e) {
            throw new RuntimeException("Error seeding UnitTypes", e);
        }
    }
}
