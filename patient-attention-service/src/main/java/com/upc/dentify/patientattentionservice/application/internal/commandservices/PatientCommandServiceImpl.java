package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.config.RabbitConfig;
import com.upc.dentify.patientattentionservice.domain.model.aggregates.Patient;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePatientCommand;
import com.upc.dentify.patientattentionservice.domain.model.events.UserCreatedEvent;
import com.upc.dentify.patientattentionservice.domain.model.events.UserUpdatedEvent;
import com.upc.dentify.patientattentionservice.domain.model.valueobjects.Address;
import com.upc.dentify.patientattentionservice.domain.services.PatientCommandService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientCommandServiceImpl implements PatientCommandService {
    private final PatientRepository patientRepository;
    private final Logger log = LoggerFactory.getLogger(PatientCommandServiceImpl.class);

    public PatientCommandServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Optional<Patient> handle(UpdatePatientCommand command) {
        return patientRepository.findById(command.patientId())
                .map(patient -> {
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

                    patient.updateAdditionalInfo(
                            command.gender(),
                            address,
                            command.phoneNumber()
                    );

                    return patientRepository.save(patient);
                });
    }

    @Override
    @RabbitListener(queues = RabbitConfig.USER_CREATED_QUEUE)
    public void handle(UserCreatedEvent event) {
        log.info("Recibido UserCreatedEvent: userId={}", event.getUserId());

        // idempotencia: evita crear duplicados
        if (patientRepository.existsByUserId(event.getUserId())) {
            log.info("Paciente ya existe para userId {}, ignorando.", event.getUserId());
            return;
        }

        if(event.getRole() != 3L) {
            throw new IllegalArgumentException("Role must be 3L: Patient");
        }

        var patient = new Patient(event.getUserId(),
                event.getFirstName(),
                event.getLastName(),
                event.getBirthDate(),
                event.getEmail(),
                event.getClinicId());

        try {
            patientRepository.save(patient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @RabbitListener(queues = RabbitConfig.USER_UPDATED_QUEUE)
    public void handle(UserUpdatedEvent event) {
        log.info("Recibido UserUpdatedEvent: userId={}", event.getUserId());

        patientRepository.findByUserId(event.getUserId()).ifPresent(patient -> {
            patient.updateBasicInfo(
                    event.getFirstName(),
                    event.getLastName()
            );
            patientRepository.save(patient);
        });
    }
}
