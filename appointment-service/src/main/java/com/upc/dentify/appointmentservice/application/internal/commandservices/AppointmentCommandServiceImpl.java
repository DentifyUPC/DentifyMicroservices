package com.upc.dentify.appointmentservice.application.internal.commandservices;

import com.upc.dentify.appointmentservice.application.internal.outboundservices.ExternalClinicService;
import com.upc.dentify.appointmentservice.application.internal.outboundservices.ExternalOdontologistService;
import com.upc.dentify.appointmentservice.application.internal.outboundservices.ExternalPatientService;
import com.upc.dentify.appointmentservice.application.internal.outboundservices.ExternalShiftService;
import com.upc.dentify.appointmentservice.domain.events.AppointmentCreatedEvent;
import com.upc.dentify.appointmentservice.domain.model.aggregates.Appointment;
import com.upc.dentify.appointmentservice.domain.model.command.CreateAppointmentCommand;
import com.upc.dentify.appointmentservice.domain.model.command.UpdateAppointmentCommand;
import com.upc.dentify.appointmentservice.domain.model.valueobjects.State;
import com.upc.dentify.appointmentservice.domain.services.AppointmentCommandService;
import com.upc.dentify.appointmentservice.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import com.upc.dentify.appointmentservice.messaging.AppointmentCreatedDomainEvent;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class AppointmentCommandServiceImpl implements AppointmentCommandService {
    private final AppointmentRepository appointmentRepository;
    private final ExternalOdontologistService externalOdontologistService;
    private final ExternalShiftService externalShiftService;
    private final ExternalPatientService externalPatientService;
    private final ExternalClinicService externalClinicService;
    private final ApplicationEventPublisher eventPublisher;

    public AppointmentCommandServiceImpl(AppointmentRepository appointmentRepository,
                                         ExternalOdontologistService externalOdontologistService,
                                         ExternalShiftService externalShiftService,
                                         ExternalPatientService externalPatientService,
                                         ExternalClinicService externalClinicService,
                                         ApplicationEventPublisher eventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.externalOdontologistService = externalOdontologistService;
        this.externalShiftService = externalShiftService;
        this.externalPatientService = externalPatientService;
        this.externalClinicService = externalClinicService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public Optional<Appointment> handle(CreateAppointmentCommand command) {
        if (!externalClinicService.existsByClinicId(command.clinicId())) {
            throw new IllegalArgumentException("Clinic not found");
        }

        var patients = externalPatientService.getPatientsByClinicId(command.clinicId());
        boolean patientExists = patients.stream()
                .anyMatch(p -> p.id().equals(command.patientId()));
        if (!patientExists) {
            throw new IllegalArgumentException("Patient not found in clinic " + command.clinicId());
        }

        var odontologists = externalOdontologistService.getAllOdontologistsByClinicId(command.clinicId());
        boolean odontologistExists = odontologists.stream()
                .anyMatch(o -> o.id().equals(command.odontologistId()));
        if (!odontologistExists) {
            throw new IllegalArgumentException("Odontologist not found in clinic " + command.clinicId());
        }

        var shifts = externalShiftService.getAllShiftsByClinicId(command.clinicId());
        var selectedShifts = shifts.stream()
                .filter(s -> s.name().equalsIgnoreCase(command.shiftName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Shift not found in clinic " + command.clinicId()));
        if (command.startTime().isBefore(selectedShifts.startTime()) ||
        command.endTime().isAfter(selectedShifts.endTime())) {
            throw new IllegalArgumentException("Start time and end time must be in the range of shift time");
        }
        if (command.endTime().isBefore(command.startTime())) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }

        //Validar solapamiento de horario de cita
        var overlappingAppointments = appointmentRepository.findAllByOdontologistId(command.odontologistId())
                .stream()
                .filter(a -> a.getAppointmentDate().isEqual(command.appointmentDate()))
                .filter(a -> command.startTime().isBefore(a.getEndTime()) &&
                        command.endTime().isAfter(a.getStartTime()))
                .toList();
        if (!overlappingAppointments.isEmpty()) {
            throw new IllegalArgumentException("Overlapping appointments found");
        }

        //Validar que no se registre en fechas pasadas
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        if(command.appointmentDate().isBefore(today)) {
            throw new IllegalArgumentException("Appointment date cannot be in the past");
        }
        if (command.appointmentDate().isEqual(today) && command.startTime().isBefore(now)) {
            throw new IllegalArgumentException("Start time must be later than current time");
        }

        //Validar 6 horas máx de citas en el mismo día
        var existingAppointments = appointmentRepository.findAllByOdontologistId(command.odontologistId());
        var sameDayAppointments = existingAppointments.stream()
                .filter(a -> a.getAppointmentDate().equals(command.appointmentDate()))
                .toList();
        double totalHours = sameDayAppointments.stream()
                .mapToDouble(a -> Duration.between(a.getStartTime(), a.getEndTime()).toMinutes() / 60.0)
                .sum();

        double newAppointmentHours = Duration.between(command.startTime(), command.endTime()).toMinutes() / 60.0;

        if (totalHours + newAppointmentHours > 6.0) {
            throw new IllegalArgumentException("Odontologist had complete the maximum of 6 hours");
        }

        Appointment appointment = new Appointment(command);
        appointment.setServiceId(command.serviceId());
        appointmentRepository.save(appointment);

        AppointmentCreatedEvent payload = new AppointmentCreatedEvent(
                appointment.getId(),
                appointment.getOdontologistId(),
                appointment.getPatientId(),
                command.clinicId(),
                command.serviceId()
        );
        eventPublisher.publishEvent(new AppointmentCreatedDomainEvent(payload));

        return Optional.of(appointment);
    }

    @Override
    public Optional<Appointment> handle(UpdateAppointmentCommand command) {
        if (appointmentRepository.findById(command.id()).isEmpty()) {
            throw new IllegalArgumentException("Appointment not found");
        }

        var appointment = appointmentRepository.findById(command.id());

        appointment.get().setState(State.valueOf(command.state()));

        try {
            var updatedAppointment = appointmentRepository.save(appointment.get());
            return Optional.of(updatedAppointment);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("An error occurred while updating appointment " + e.getMessage());
        }
    }
}
