package com.upc.dentify.appointmentservice.domain.model.aggregates;

import com.upc.dentify.appointmentservice.domain.model.command.CreateAppointmentCommand;
import com.upc.dentify.appointmentservice.domain.model.valueobjects.State;
import org.junit.jupiter.api.DisplayName;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Validation appointment entity")
class AppointmentTest {

    @Test
    void constructorShouldSetInitialStatePending() {
        CreateAppointmentCommand cmd = new CreateAppointmentCommand(
                1L, 2L,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalDate.of(2025, 12, 1),
                "Mañana",
                5L
        );

        Appointment appointment = new Appointment(cmd);

        assertThat(appointment.getState()).isEqualTo(State.PENDING);
    }

    @Test
    void constructorShouldFailWhenCommandIsNull() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Appointment(null));
    }

}