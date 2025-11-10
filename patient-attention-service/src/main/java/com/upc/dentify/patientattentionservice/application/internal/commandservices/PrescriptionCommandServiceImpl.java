package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Prescription;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePrescriptionCommand;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetPrescriptionByIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.PrescriptionCommandService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrescriptionCommandServiceImpl implements PrescriptionCommandService {
    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionCommandServiceImpl(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public Optional<Prescription> handle(UpdatePrescriptionCommand command) {
        if(prescriptionRepository.findById(command.id()).isEmpty()) {
            throw new IllegalArgumentException("Prescription with Id: " + command.id() + " not found");
        }

        var prescription = prescriptionRepository.findById(command.id());
        prescription.get().setEffects(command.effects());

        try {
            var updatedPrescription = prescriptionRepository.save(prescription.get());
            return Optional.of(updatedPrescription);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("An error occurred while updating prescription", e);
        }
    }
}
