package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.OdontogramItem;
import com.upc.dentify.patientattentionservice.domain.model.aggregates.ToothStatus;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateOdontogramItemCommand;
import com.upc.dentify.patientattentionservice.domain.services.OdontogramItemCommandService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.OdontogramItemRepository;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.ToothStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OdontogramItemCommandServiceImpl implements OdontogramItemCommandService {

    private final OdontogramItemRepository odontogramItemRepository;
    private final ToothStatusRepository toothStatusRepository;

    public OdontogramItemCommandServiceImpl(OdontogramItemRepository odontogramItemRepository,
                                            ToothStatusRepository toothStatusRepository) {
        this.odontogramItemRepository = odontogramItemRepository;
        this.toothStatusRepository = toothStatusRepository;
    }

    @Override
    public Optional<OdontogramItem> handle(UpdateOdontogramItemCommand command) {

        if(odontogramItemRepository.findById(command.id()).isEmpty()) {
            throw new IllegalArgumentException("Odontogram item with id " + command.id() + " does not exist");
        }

        if(!toothStatusRepository.existsById(command.idToothStatus())) {
            throw new IllegalArgumentException("Tooth status with id " + command.idToothStatus() + " does not exist");
        }

        var odontogramItem = odontogramItemRepository.findById(command.id());
        odontogramItem.get().setToothStatus(new ToothStatus(command.idToothStatus()));

        try {
            var updatedOdontogramItem =  odontogramItemRepository.save(odontogramItem.get());
            return Optional.of(updatedOdontogramItem);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Could not update odontogram item", e);
        }


    }
}
