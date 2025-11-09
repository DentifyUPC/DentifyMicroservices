package com.upc.dentify.patientattentionservice.infrastructure.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.dentify.patientattentionservice.domain.model.aggregates.Teeth;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.TeethRepository;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.TeethResourceFromJson;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class TeethSeeder {

    private final TeethRepository teethRepository;
    private final ObjectMapper objectMapper;

    public TeethSeeder(TeethRepository teethRepository, ObjectMapper objectMapper) {
        this.teethRepository = teethRepository;
        this.objectMapper = objectMapper;
    }

    public void seed() {

        try (InputStream inputStream = getClass().getResourceAsStream("/seed/teeth.json")) {
            List<TeethResourceFromJson> toothDtos =
                    objectMapper.readValue(inputStream, new TypeReference<>() {});

            for (TeethResourceFromJson dto : toothDtos) {
//                if (teethRepository.findByCode(dto.code()).isPresent()) {
//                    throw new IllegalArgumentException("Teeth with code " + dto.code() + " already exists");
//                }
//
//                teethRepository.save(new Teeth(dto.code(), dto.name()));

                teethRepository.findByCode(dto.code())
                        .orElseGet(() -> teethRepository.save(new Teeth(dto.code(), dto.name())));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error seeding tooth", e);
        }

    }
}
