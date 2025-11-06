package com.upc.dentify.clinicmanagementservice.application.internal.commandservices;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.SchedulePerClinic;
import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.Shift;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateShiftCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.DeleteShiftCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateShiftCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.valueobjects.ShiftName;
import com.upc.dentify.clinicmanagementservice.domain.services.ShiftCommandService;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ClinicRepository;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.SchedulePerClinicRepository;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ShiftRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShiftCommandServiceImpl implements ShiftCommandService {

    private final ShiftRepository shiftRepository;
    private final ClinicRepository clinicRepository;
    private final SchedulePerClinicRepository schedulePerClinicRepository;

    public ShiftCommandServiceImpl(ShiftRepository shiftRepository,
                                   ClinicRepository clinicRepository,
                                   SchedulePerClinicRepository schedulePerClinicRepository) {
        this.shiftRepository = shiftRepository;
        this.clinicRepository = clinicRepository;
        this.schedulePerClinicRepository = schedulePerClinicRepository;
    }

    @Override
    public Optional<Shift> handle(CreateShiftCommand command) {

        if(!clinicRepository.existsById(command.clinicId())) {
            throw new IllegalArgumentException("This clinic does not exist");
        }

        if(shiftRepository.existsByNameAndClinicId(ShiftName.valueOf(command.name()), command.clinicId())) {
            throw new IllegalArgumentException("This shift already exists in the clinic");
        }

        if(schedulePerClinicRepository.findByClinicId(command.clinicId()).isEmpty()) {
            throw new IllegalArgumentException("This clinic does not have a schedule");
        }

        var schedulePerClinic = schedulePerClinicRepository.findByClinicId(command.clinicId());

        if (command.startTime().isBefore(schedulePerClinic.get().getStartTime()) ||
                command.endTime().isAfter(schedulePerClinic.get().getEndTime())) {

            throw new IllegalArgumentException(
                    String.format("Shift must be within clinic schedule: [%s - %s]",
                            schedulePerClinic.get().getStartTime(), schedulePerClinic.get().getEndTime())
            );
        }

        var newShift = new Shift(command);

        try {
            shiftRepository.save(newShift);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving shift", e);
        }

        return Optional.of(newShift);
    }

    @Override
    public Optional<Shift> handle(UpdateShiftCommand command) {
        if(shiftRepository.findById(command.shiftId()).isEmpty()) {
            throw new IllegalArgumentException("This shift does not exist");
        }

        var shift = shiftRepository.findById(command.shiftId());

        var schedulePerClinic = schedulePerClinicRepository.findByClinicId(shift.get().getClinic().getId());

        if (command.startTime().isBefore(schedulePerClinic.get().getStartTime()) ||
                command.endTime().isAfter(schedulePerClinic.get().getEndTime())) {

            throw new IllegalArgumentException(
                    String.format("Shift must be within clinic schedule: [%s - %s]",
                            schedulePerClinic.get().getStartTime(), schedulePerClinic.get().getEndTime())
            );
        }

        shift.get().setStartTime(command.startTime());
        shift.get().setEndTime(command.endTime());

        try {
            var updatedShift = shiftRepository.save(shift.get());
            return Optional.of(updatedShift);
        } catch(RuntimeException e) {
            throw new IllegalArgumentException("An error occurred while updating the shift" + e.getMessage());
        }

    }

    @Override
    public void handle(DeleteShiftCommand command) {
        if(shiftRepository.findById(command.shiftId()).isEmpty()) {
            throw new IllegalArgumentException("This shift does not exist");
        }

        try {
            shiftRepository.deleteById(command.shiftId());
        } catch(RuntimeException e) {
            throw new IllegalArgumentException("An error occurred while deleting the shift" + e.getMessage());
        }
    }
}
