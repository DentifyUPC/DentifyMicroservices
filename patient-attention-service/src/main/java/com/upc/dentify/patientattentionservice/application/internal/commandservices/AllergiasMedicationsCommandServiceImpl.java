package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.AllergiasMedications;
import com.upc.dentify.patientattentionservice.domain.model.commands.CreateAllergiasMedicationsCommand;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateAllergiasMedicationsCommand;
import com.upc.dentify.patientattentionservice.domain.services.AllergiasMedicationsCommandService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.AllergiasMedicationsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AllergiasMedicationsCommandServiceImpl implements AllergiasMedicationsCommandService {
    private final AllergiasMedicationsRepository allergiasMedicationsRepository;

    public AllergiasMedicationsCommandServiceImpl(AllergiasMedicationsRepository allergiasMedicationsRepository) {
        this.allergiasMedicationsRepository = allergiasMedicationsRepository;
    }

    @Override
    public AllergiasMedications handle(CreateAllergiasMedicationsCommand command) {
        var allergiasMedications = new AllergiasMedications(command.medicationName(), command.clinicalRecordId());
        return allergiasMedicationsRepository.save(allergiasMedications);
    }

    @Override
    public Optional<AllergiasMedications> handle(UpdateAllergiasMedicationsCommand command) {
        return allergiasMedicationsRepository.findById(command.id())
                .map(allergiasMedications -> {
                    allergiasMedications.setMedicationName(command.medicationName());
                    allergiasMedications.setUpdatedAt(LocalDateTime.now());
                    return allergiasMedicationsRepository.save(allergiasMedications);
                });
    }
}
