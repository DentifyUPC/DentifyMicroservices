package com.upc.dentify.clinicmanagementservice.application.internal.commandservices;

import com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl.ExternalItemPerServiceService;
import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ServicesPerClinics;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateServicePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateServicePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.services.ServicePerClinicCommandService;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ItemPerClinicRepository;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ServicePerClinicRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicePerClinicCommandServiceImpl implements ServicePerClinicCommandService {
    private final ServicePerClinicRepository servicePerClinicRepository;
    private final ExternalItemPerServiceService externalItemPerServiceService;
    private final ItemPerClinicRepository itemPerClinicRepository;

    public ServicePerClinicCommandServiceImpl(ServicePerClinicRepository servicePerClinicRepository, ExternalItemPerServiceService externalItemPerServiceService, ItemPerClinicRepository itemPerClinicRepository) {
        this.servicePerClinicRepository = servicePerClinicRepository;
        this.externalItemPerServiceService = externalItemPerServiceService;
        this.itemPerClinicRepository = itemPerClinicRepository;
    }


    @Override
    public Optional<ServicesPerClinics> handle(CreateServicePerClinicCommand command) {
        if (servicePerClinicRepository.existsByClinicIdAndServiceId(command.clinicId(), command.serviceId())) {
            throw new IllegalArgumentException("Clinic with id " + command.clinicId() + " and service with id " + command.serviceId() + " already exists");
        }
        var requiredItems = externalItemPerServiceService.getItemsIdsByServiceId(command.serviceId());
        var clinicItems = itemPerClinicRepository.findAllByClinicId(command.clinicId());

        for (var requiredItem : requiredItems) {
            var clinicItem = clinicItems.stream()
                    .filter(ipc -> ipc.getItemId().equals(requiredItem.id()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            if (clinicItem.getAvailableStock() < requiredItem.quantityRequired()) {
                throw new IllegalArgumentException(
                        "Clinic " + command.clinicId() + " doesn't have enough stock"
                );
            }
        }

        double totalPricePerItems = requiredItems.stream()
                .mapToDouble(required -> {
                    var clinicItem = clinicItems.stream()
                            .filter(ipc -> ipc.getItemId().equals(required.id()))
                            .findFirst()
                            .get();
                    return clinicItem.getPrice() * required.quantityRequired();
                })
                .sum();

        var servicesPerClinics = new ServicesPerClinics(command.clinicId(), command.serviceId(), command.totalLaborPrice());
        servicesPerClinics.calculateTotals(totalPricePerItems);

        var saved = servicePerClinicRepository.save(servicesPerClinics);

        return Optional.of(saved);
    }

    @Override
    public Optional<ServicesPerClinics> handle(UpdateServicePerClinicCommand command) {
        var servicePerClinic = servicePerClinicRepository.findById(command.id());
        servicePerClinic.get().updateTotals(command.totalLaborPrice());

        try {
            var updatedServicePerClinic = servicePerClinicRepository.save(servicePerClinic.get());
            return Optional.of(updatedServicePerClinic);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("An error occurred while updating service per clinic", e);
        }
    }
}
