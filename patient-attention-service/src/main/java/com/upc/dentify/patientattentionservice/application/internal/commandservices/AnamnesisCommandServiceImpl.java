package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Anamnesis;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateAnamnesisCommand;
import com.upc.dentify.patientattentionservice.domain.model.valueobjects.PhoneNumber;
import com.upc.dentify.patientattentionservice.domain.services.AnamnesisCommandService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.AnamnesisRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnamnesisCommandServiceImpl implements AnamnesisCommandService {
    private final AnamnesisRepository anamnesisRepository;

    public AnamnesisCommandServiceImpl(AnamnesisRepository anamnesisRepository) {
        this.anamnesisRepository = anamnesisRepository;
    }

    @Override
    public Optional<Anamnesis> handle(UpdateAnamnesisCommand command) {
        return anamnesisRepository.findById(command.id())
                .map(anamnesis -> {
                    anamnesis.setClinicalBackground(command.clinicalBackground());
                    anamnesis.setLowBloodPressure(command.lowBloodPressure());
                    anamnesis.setHighBloodPressure(command.highBloodPressure());
                    anamnesis.setSmoke(command.smoke());
                    anamnesis.setCurrentMedications(command.currentMedications());
                    anamnesis.setEmergencyContact(new PhoneNumber(command.emergencyContact()));

                    return anamnesisRepository.save(anamnesis);
                });
    }
}