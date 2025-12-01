package com.upc.dentify.appointmentservice.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.upc.dentify.appointmentservice.domain.model.command.CreateAppointmentCommand;
import com.upc.dentify.appointmentservice.domain.model.valueobjects.State;
import com.upc.dentify.appointmentservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Appointment extends AuditableAbstractAggregateRoot<Appointment> {
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "odontologist_id", nullable = false)
    private Long odontologistId;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "shift_name", nullable = false)
    private String shiftName;

    @Column(name = "clinic_id", nullable = false)
    private Long clinicId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    public Appointment() {}

    public Appointment(CreateAppointmentCommand command) {
        this.state = State.PENDING;
        this.patientId = command.patientId();
        this.odontologistId = command.odontologistId();
        this.startTime = command.startTime();
        this.endTime = command.endTime();
        this.appointmentDate = command.appointmentDate();
        this.shiftName = command.shiftName();
        this.clinicId = command.clinicId();
        this.serviceId = command.serviceId();
    }
}
