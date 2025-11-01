package com.upc.dentify.clinicmanagementservice.application.internal.commandservices;

import com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl.ExternalItemService;
import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ItemPerClinic;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateItemPerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateItemPerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.services.ItemPerClinicCommandService;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ItemPerClinicRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemPerClinicCommandServiceImpl implements ItemPerClinicCommandService {

    private final ItemPerClinicRepository itemPerClinicRepository;
    private final ExternalItemService externalItemService;

    public ItemPerClinicCommandServiceImpl(ItemPerClinicRepository itemPerClinicRepository,
                                           ExternalItemService externalItemService) {
        this.itemPerClinicRepository = itemPerClinicRepository;
        this.externalItemService = externalItemService;
    }

    @Override
    public Optional<ItemPerClinic> handle(CreateItemPerClinicCommand command) {

        //comprobar si existe item en el catalogo
        if(!externalItemService.existsById(command.itemId())) {
            throw new IllegalArgumentException("This item does not exist in the catalog");
        }

        if(itemPerClinicRepository.existsByClinicIdAndItemId(command.clinicId(), command.itemId())) {
            throw new IllegalArgumentException("This item already exists in the clinic");
        }

        var newItemPerClinic = new ItemPerClinic(command);

        try {
            itemPerClinicRepository.save(newItemPerClinic);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving item per clinic", e);
        }

        return Optional.of(newItemPerClinic);
    }

    @Override
    public Optional<ItemPerClinic> handle(UpdateItemPerClinicCommand command) {

        var itemPerClinic = itemPerClinicRepository.findById(command.id());
        itemPerClinic.get().update(command);

        try {
            var updatedItemPerClinic = itemPerClinicRepository.save(itemPerClinic.get());
            return Optional.of(updatedItemPerClinic);
        } catch(RuntimeException e) {
            throw new IllegalArgumentException("An error occurred while updating item per clinic" + e.getMessage());
        }
    }
}
