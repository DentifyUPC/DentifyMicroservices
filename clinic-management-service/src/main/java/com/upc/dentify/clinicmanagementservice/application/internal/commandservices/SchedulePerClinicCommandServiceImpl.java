package com.upc.dentify.clinicmanagementservice.application.internal.commandservices;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.SchedulePerClinic;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateSchedulePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateSchedulePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.services.SchedulePerClinicCommandService;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ClinicRepository;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.SchedulePerClinicRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SchedulePerClinicCommandServiceImpl implements SchedulePerClinicCommandService {

    private final SchedulePerClinicRepository schedulePerClinicRepository;
    private final ClinicRepository clinicRepository;

    public SchedulePerClinicCommandServiceImpl(SchedulePerClinicRepository schedulePerClinicRepository,
                                               ClinicRepository clinicRepository) {
        this.schedulePerClinicRepository = schedulePerClinicRepository;
        this.clinicRepository = clinicRepository;
    }

    @Override
    public Optional<SchedulePerClinic> handle(CreateSchedulePerClinicCommand command) {

        if(!clinicRepository.existsById(command.clinicId())) {
            throw new IllegalArgumentException("This clinic does not exist");
        }

        if(schedulePerClinicRepository.findByClinicId(command.clinicId()).isPresent()) {
            throw new IllegalArgumentException("Clinic with id " + command.clinicId() + " already has a schedule");
        }

        var newSchedulePerClinic = new SchedulePerClinic(command);

        try {
            schedulePerClinicRepository.save(newSchedulePerClinic);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving schedule per clinic", e);
        }

        return Optional.of(newSchedulePerClinic);
    }

    @Override
    public Optional<SchedulePerClinic> handle(UpdateSchedulePerClinicCommand command) {

        if(!clinicRepository.existsById(command.clinicId())) {
            throw new IllegalArgumentException("This clinic does not exist");
        }

        if(schedulePerClinicRepository.findByClinicId(command.clinicId()).isEmpty()) {
            throw new IllegalArgumentException("Clinic with id " + command.clinicId() + " does not have a schedule per clinic");
        }

        var schedulePerClinic = schedulePerClinicRepository.findByClinicId(command.clinicId());
        schedulePerClinic.get().setStartTime(command.startTime());
        schedulePerClinic.get().setEndTime(command.endTime());

        try {
            var updatedSchedulePerClinic = schedulePerClinicRepository.save(schedulePerClinic.get());
            return Optional.of(updatedSchedulePerClinic);
        } catch(RuntimeException e) {
            throw new IllegalArgumentException("An error occurred while updating schedule per clinic" + e.getMessage());
        }

    }
}
