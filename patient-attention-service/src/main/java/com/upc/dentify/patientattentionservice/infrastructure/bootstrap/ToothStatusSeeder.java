package com.upc.dentify.patientattentionservice.infrastructure.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.dentify.patientattentionservice.domain.model.aggregates.ToothStatus;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.ToothStatusRepository;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.ToothStatusResourceFromJson;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class ToothStatusSeeder {

    private final ToothStatusRepository toothStatusRepository;
    private final ObjectMapper objectMapper;

    public ToothStatusSeeder(ToothStatusRepository toothStatusRepository, ObjectMapper objectMapper) {
        this.toothStatusRepository = toothStatusRepository;
        this.objectMapper = objectMapper;
    }

    public void seed() {

        try (InputStream inputStream = getClass().getResourceAsStream("/seed/tooth_status.json")) {
            List<ToothStatusResourceFromJson> status =
                    objectMapper.readValue(inputStream, new TypeReference<>() {});

            for (ToothStatusResourceFromJson dto : status) {
//                if (toothStatusRepository.findByName(dto.name()).isPresent()) {
//                    throw new IllegalStateException("Tooth status already exists");
//                }
//
//                toothStatusRepository.save(new ToothStatus(dto.name()));

                toothStatusRepository.findByName(dto.name())
                        .orElseGet(() -> toothStatusRepository.save(new ToothStatus(dto.name())));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error seeding tooth status", e);
        }

    }

}
