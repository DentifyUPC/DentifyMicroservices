package com.upc.dentify.iam.infrastructure.bootstrap;

import com.upc.dentify.iam.domain.model.entities.IdentificationType;
import com.upc.dentify.iam.infrastructure.persistence.jpa.repositories.IdentificationTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class IdentificationTypeSeeder implements CommandLineRunner {

    private final IdentificationTypeRepository identificationTypeRepository;

    public IdentificationTypeSeeder(IdentificationTypeRepository identificationTypeRepository) {
        this.identificationTypeRepository = identificationTypeRepository;
    }

    @Override
    public void run(String... args) {
        List<String> types = List.of("DNI", "FOREIGNER ID CARD");

        for (String identificationTypeName : types) {
            identificationTypeRepository.findByName(identificationTypeName)
                    .orElseGet(() -> identificationTypeRepository.save(new IdentificationType(identificationTypeName)));
        }
    }
}
