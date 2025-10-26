package com.upc.dentify.practicemanagementservice.application.internal.commandservices;

import com.upc.dentify.practicemanagementservice.config.RabbitConfig;
import com.upc.dentify.practicemanagementservice.domain.model.aggregates.Odontologist;
import com.upc.dentify.practicemanagementservice.domain.model.commands.UpdateOdontologistCommand;
import com.upc.dentify.practicemanagementservice.domain.model.events.UserCreatedEvent;
import com.upc.dentify.practicemanagementservice.domain.model.events.UserUpdatedEvent;
import com.upc.dentify.practicemanagementservice.domain.model.valueobjects.Address;
import com.upc.dentify.practicemanagementservice.domain.services.OdontologistCommandService;
import com.upc.dentify.practicemanagementservice.infrastructure.persistence.jpa.repositories.OdontologistRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class OdontologistCommandServiceImpl implements OdontologistCommandService {
    private final OdontologistRepository odontologistRepository;
    private final Logger log = LoggerFactory.getLogger(OdontologistCommandServiceImpl.class);
    //private final ExternalServicePerClinicService externalServicePerClinicService;

    public OdontologistCommandServiceImpl(OdontologistRepository odontologistRepository
                                          //ExternalServicePerClinicService externalServicePerClinicService
    ) {
        this.odontologistRepository = odontologistRepository;
        //this.externalServicePerClinicService = externalServicePerClinicService;
    }


    @Override
    public Optional<Odontologist> handle(UpdateOdontologistCommand command) {
        return odontologistRepository.findById(command.odontologistId())
                .map(odontologist -> {
//                    if (command.serviceId() != null) {
//                        Long clinicId = odontologist.getClinicId();
//                        boolean exists = externalServicePerClinicService
//                                .existsByClinicIdAndServiceId(clinicId, command.serviceId());
//
//                        if (!exists) {
//                            throw new IllegalArgumentException(
//                                    "Service id " + command.serviceId() +
//                                            " is not available in Clinic " + clinicId
//                            );
//                        }
//                    }

                    Address address = null;
                    if (command.street() != null &&
                            command.department() != null &&
                            command.district() != null &&
                            command.province() != null) {
                        address = new Address(
                                command.street(),
                                command.district(),
                                command.department(),
                                command.province()
                        );
                    }

                    odontologist.updateAdditionalInformation(
                            command.gender(),
                            address,
                            command.phoneNumber(),
                            command.professionalLicenseNumber(),
                            command.specialtyRegistrationNumber(),
                            command.specialty(),
                            command.serviceId(),
                            command.isActive()
                    );

                    return odontologistRepository.save(odontologist);
                });
    }

    @Override
    @RabbitListener(queues = RabbitConfig.USER_CREATED_QUEUE)
    public void handle(UserCreatedEvent event) {
        log.info("Recibido UserCreatedEvent: userId={}", event.getUserId());

        // idempotencia: evita crear duplicados
        if (odontologistRepository.existsByUserId(event.getUserId())) {
            log.info("Paciente ya existe para userId {}, ignorando.", event.getUserId());
            return;
        }

        if(event.getRole() != 2L) {
            throw new IllegalArgumentException("Role must be 2L: Odontologist");
        }

        var odontologist = new Odontologist(event.getUserId(),
                event.getFirstName(),
                event.getLastName(),
                event.getBirthDate(),
                event.getEmail(),
                event.getClinicId());

        try {
            odontologistRepository.save(odontologist);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @RabbitListener(queues = RabbitConfig.USER_UPDATED_QUEUE)
    public void handle(UserUpdatedEvent event) {
        log.info("Recibido UserUpdatedEvent: userId={}", event.getUserId());

        if(event.getRole() != 2L) {
            throw new IllegalArgumentException("Role must be 2L: Odontologist");
        }

        odontologistRepository.findByUserId(event.getUserId()).ifPresent(odontologist -> {
            odontologist.updateBasicInfo(
                    event.getFirstName(),
                    event.getLastName()
            );
            odontologistRepository.save(odontologist);
        });
    }
}