package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.PrescriptionItems;
import com.upc.dentify.patientattentionservice.domain.model.commands.CreatePrescriptionItemCommand;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePrescriptionItemCommand;
import com.upc.dentify.patientattentionservice.domain.services.PrescriptionItemCommandService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PrescriptionItemRepository;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrescriptionItemCommandServiceImpl implements PrescriptionItemCommandService {
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionItemCommandServiceImpl(PrescriptionItemRepository prescriptionItemRepository,
                                              PrescriptionRepository prescriptionRepository) {
        this.prescriptionItemRepository = prescriptionItemRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public Optional<PrescriptionItems> handle(CreatePrescriptionItemCommand command) {
        if(!prescriptionRepository.existsById(command.prescriptionId())) {
            throw new IllegalArgumentException("Invalid prescription id");
        }

        var newPrescriptionItem = new PrescriptionItems(command);

        try {
            prescriptionItemRepository.save(newPrescriptionItem);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving prescription item", e);
        }
        return Optional.of(newPrescriptionItem);
    }

    @Override
    public Optional<PrescriptionItems> handle(UpdatePrescriptionItemCommand command) {
        if(prescriptionItemRepository.findById(command.id()).isEmpty()) {
            throw new IllegalArgumentException("Invalid prescription item id");
        }
        var prescriptionItem = prescriptionItemRepository.findById(command.id());
        prescriptionItem.get().setMedicationName(command.medicationName());
        prescriptionItem.get().setDescription(command.description());
        try {
            var updatedPrescriptionItem = prescriptionItemRepository.save(prescriptionItem.get());
            return Optional.of(updatedPrescriptionItem);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error updating prescription item");
        }
    }
}