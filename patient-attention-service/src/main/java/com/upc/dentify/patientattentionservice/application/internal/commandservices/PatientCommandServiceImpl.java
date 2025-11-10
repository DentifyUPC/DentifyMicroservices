package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.config.RabbitConfig;
import com.upc.dentify.patientattentionservice.domain.model.aggregates.*;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePatientCommand;
import com.upc.dentify.patientattentionservice.domain.model.events.UserCreatedEvent;
import com.upc.dentify.patientattentionservice.domain.model.events.UserUpdatedEvent;
import com.upc.dentify.patientattentionservice.domain.model.valueobjects.Address;
import com.upc.dentify.patientattentionservice.domain.services.PatientCommandService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PatientCommandServiceImpl implements PatientCommandService {
    private final PatientRepository patientRepository;
    private final AnamnesisRepository anamnesisRepository;
    private final OdontogramRepository odontogramRepository;
    private final OdontogramItemRepository odontogramItemRepository;
    private final TeethRepository teethRepository;
    private final ToothStatusRepository toothStatusRepository;
    private final ClinicalRecordRepository clinicalRecordRepository;
    private final Logger log = LoggerFactory.getLogger(PatientCommandServiceImpl.class);

    public PatientCommandServiceImpl(PatientRepository patientRepository,
                                     AnamnesisRepository anamnesisRepository,
                                     OdontogramRepository odontogramRepository,
                                     TeethRepository teethRepository,
                                     ToothStatusRepository toothStatusRepository,
                                     OdontogramItemRepository odontogramItemRepository,
                                     ClinicalRecordRepository clinicalRecordRepository) {
        this.patientRepository = patientRepository;
        this.anamnesisRepository = anamnesisRepository;
        this.odontogramRepository = odontogramRepository;
        this.teethRepository = teethRepository;
        this.toothStatusRepository = toothStatusRepository;
        this.odontogramItemRepository = odontogramItemRepository;
        this.clinicalRecordRepository = clinicalRecordRepository;
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
    @Transactional
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

        //patient
        var patient = new Patient(event.getUserId(),
                event.getFirstName(),
                event.getLastName(),
                event.getBirthDate(),
                event.getEmail(),
                event.getClinicId());

        var patientResponse = patientRepository.save(patient);

        //anamnesis
        var anamnesis = new Anamnesis();

        var anamnesisResponse = anamnesisRepository.save(anamnesis);

        //odontogram
        var odontogram = new Odontogram();

        var odontogramResponse = odontogramRepository.save(odontogram);

        //odontogram items
        var allTeeth = teethRepository.findAll();

        ToothStatus defaultStatus = toothStatusRepository.findByName("Sano")
                .orElseThrow(() -> new IllegalStateException("No tooth statuses configured"));

        var items = new ArrayList<OdontogramItem>();
        for (Teeth tooth : allTeeth) {
            var item = new OdontogramItem();
            item.setTeeth(tooth);
            item.setToothStatus(defaultStatus);
            item.setOdontogram(odontogram);
            items.add(item);
        }

        if (!items.isEmpty()) {
            odontogramItemRepository.saveAll(items);
        }

        var clinicalRecord = new ClinicalRecords(patientResponse.getId(),
                anamnesisResponse.getId(), odontogramResponse.getId());

        clinicalRecordRepository.save(clinicalRecord);
    }

    @Override
    @RabbitListener(queues = RabbitConfig.USER_UPDATED_QUEUE)
    public void handle(UserUpdatedEvent event) {
        log.info("Recibido UserUpdatedEvent: userId={}", event.getUserId());

        if(event.getRole() != 3L) {
            throw new IllegalArgumentException("Role must be 3L: Patient");
        }

        patientRepository.findByUserId(event.getUserId()).ifPresent(patient -> {
            patient.updateBasicInfo(
                    event.getFirstName(),
                    event.getLastName()
            );
            patientRepository.save(patient);
        });
    }
}
