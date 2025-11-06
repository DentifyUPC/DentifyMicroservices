package com.upc.dentify.clinicmanagementservice.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateSchedulePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
public class SchedulePerClinic extends AuditableAbstractAggregateRoot<SchedulePerClinic> {

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @OneToOne
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    public SchedulePerClinic() {}

    public SchedulePerClinic(CreateSchedulePerClinicCommand command) {
        this.startTime = command.startTime();
        this.endTime = command.endTime();
        this.clinic = new Clinic(command.clinicId());
    }

}
