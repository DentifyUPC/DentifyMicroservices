package com.upc.dentify.servicecatalogservice.infrastructure.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Service;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.ServiceRepository;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ServiceResourceFromJson;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class ServiceSeeder {

    private final ServiceRepository serviceRepository;
    private final ObjectMapper objectMapper;

    public ServiceSeeder(ServiceRepository serviceRepository, ObjectMapper objectMapper) {
        this.serviceRepository = serviceRepository;
        this.objectMapper = objectMapper;
    }

    public void seed() {

        try (InputStream inputStream = getClass().getResourceAsStream("/seed/services.json")) {
            List<ServiceResourceFromJson> serviceDtos =
                    objectMapper.readValue(inputStream, new TypeReference<>() {
                    });

            for (ServiceResourceFromJson dto : serviceDtos) {
                serviceRepository.findByName(dto.name())
                        .orElseGet(() -> serviceRepository.save(new Service(dto.name())));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error seeding Services", e);
        }
    }
}
