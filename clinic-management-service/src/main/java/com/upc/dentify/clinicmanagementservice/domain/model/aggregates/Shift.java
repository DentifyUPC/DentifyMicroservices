package com.upc.dentify.clinicmanagementservice.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateShiftCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.valueobjects.ShiftName;
import com.upc.dentify.clinicmanagementservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Shift extends AuditableAbstractAggregateRoot<Shift> {
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, length = 20)
    private ShiftName name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    public Shift() {}

    public Shift(CreateShiftCommand command) {
        this.startTime = command.startTime();
        this.endTime = command.endTime();
        this.name = ShiftName.valueOf(command.name());
        this.clinic = new Clinic(command.clinicId());
    }
}
