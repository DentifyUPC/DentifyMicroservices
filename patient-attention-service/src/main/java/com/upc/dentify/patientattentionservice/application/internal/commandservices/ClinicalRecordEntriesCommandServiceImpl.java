package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.config.RabbitConfig;
import com.upc.dentify.patientattentionservice.domain.model.aggregates.ClinicalRecordEntries;
import com.upc.dentify.patientattentionservice.domain.model.aggregates.Prescription;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateClinicalRecordEntryCommand;
import com.upc.dentify.patientattentionservice.domain.model.events.AppointmentCreatedEvent;
import com.upc.dentify.patientattentionservice.domain.services.ClinicalRecordEntriesCommandService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.ClinicalRecordEntryRepository;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.ClinicalRecordRepository;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PrescriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClinicalRecordEntriesCommandServiceImpl implements ClinicalRecordEntriesCommandService {
    private final Logger log = LoggerFactory.getLogger(ClinicalRecordEntriesCommandServiceImpl.class);
    private final ClinicalRecordRepository clinicalRecordRepository;
    private final ClinicalRecordEntryRepository clinicalRecordEntryRepository;
    private final PrescriptionRepository prescriptionRepository;

    public ClinicalRecordEntriesCommandServiceImpl(ClinicalRecordRepository clinicalRecordRepository, ClinicalRecordEntryRepository clinicalRecordEntryRepository,
                                                   PrescriptionRepository prescriptionRepository) {
        this.clinicalRecordEntryRepository = clinicalRecordEntryRepository;
        this.clinicalRecordRepository = clinicalRecordRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    @Transactional
    @RabbitListener(queues = RabbitConfig.APPOINTMENT_CREATED_QUEUE)
    public void handle(AppointmentCreatedEvent event) {
        log.info("Recibido AppointmentCreatedEvent: id={}", event.getId());

        if(clinicalRecordEntryRepository.existsByAppointmentId(event.getId())) {
            log.info("Appointment already exists with id {}, ignoring", event.getId());
            return;
        }

        if(!clinicalRecordRepository.existsByPatientId(event.getPatientId())) {
            log.info("Patient does not exist with id {}, ignoring", event.getPatientId());
            return;
        }

        var prescription = new Prescription();
        prescriptionRepository.save(prescription);

        var clinicalRecord = clinicalRecordRepository.findByPatientId(event.getPatientId());

        var clinicalRecordEntry = new ClinicalRecordEntries(
                event.getOdontologistId(),
                clinicalRecord.get().getId(),
                event.getId(),
                prescription.getId()
        );

        clinicalRecordEntryRepository.save(clinicalRecordEntry);
    }

    @Override
    public Optional<ClinicalRecordEntries> handle(UpdateClinicalRecordEntryCommand command) {
        if(clinicalRecordEntryRepository.findById(command.id()).isEmpty()) {
            throw new IllegalArgumentException("Clinical record entry with id " + command.id() + " not found");
        }
        var clinicalRecordEntry = clinicalRecordEntryRepository.findById(command.id());
        clinicalRecordEntry.get().setEvolution(command.evolution());
        clinicalRecordEntry.get().setObservation(command.observation());

        try {
            var updated = clinicalRecordEntryRepository.save(clinicalRecordEntry.get());
            return Optional.of(updated);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("An error occurred while updating Clinical record entry", e);
        }
    }
}
