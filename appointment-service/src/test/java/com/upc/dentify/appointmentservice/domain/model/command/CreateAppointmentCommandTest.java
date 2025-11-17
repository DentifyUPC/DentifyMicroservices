package com.upc.dentify.appointmentservice.domain.model.command;

import com.upc.dentify.appointmentservice.domain.model.aggregates.Appointment;
import com.upc.dentify.appointmentservice.domain.model.valueobjects.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Validation create appointment command record")
class CreateAppointmentCommandTest {

    @Test
    void constructorShouldInitializeFieldsCorrectly() {
        CreateAppointmentCommand command = new CreateAppointmentCommand(
                10L,
                20L,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalDate.of(2025, 12, 1),
                "Mañana",
                5L
        );

        Appointment appt = new Appointment(command);

        assertThat(appt.getState()).isEqualTo(State.PENDING);
        assertThat(appt.getPatientId()).isEqualTo(10L);
        assertThat(appt.getOdontologistId()).isEqualTo(20L);
        assertThat(appt.getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(appt.getEndTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(appt.getAppointmentDate()).isEqualTo(LocalDate.of(2025, 12, 1));
        assertThat(appt.getShiftName()).isEqualTo("Mañana");
        assertThat(appt.getClinicId()).isEqualTo(5L);
    }

    @Test
    void shouldThrowWhenPatientIdIsNull() {
        assertThatThrownBy(() -> new CreateAppointmentCommand(
                null, 2L,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalDate.now(),
                "MORNING",
                5L
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("patientId");
    }

    @Test
    void shouldThrowWhenPatientIdIsZeroOrNegative() {
        assertThatThrownBy(() -> new CreateAppointmentCommand(
                0L, 2L,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalDate.now(),
                "Mañana",
                5L
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenStartTimeIsNull() {
        assertThatThrownBy(() -> new CreateAppointmentCommand(
                1L, 2L,
                null,
                LocalTime.of(10, 0),
                LocalDate.now(),
                "Mañana",
                5L
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenEndTimeIsNull() {
        assertThatThrownBy(() -> new CreateAppointmentCommand(
                1L, 2L,
                LocalTime.of(9, 0),
                null,
                LocalDate.now(),
                "Mañana",
                5L
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenAppointmentDateIsNull() {
        assertThatThrownBy(() -> new CreateAppointmentCommand(
                1L, 2L,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                null,
                "Mañana",
                5L
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenShiftNameIsBlank() {
        assertThatThrownBy(() -> new CreateAppointmentCommand(
                1L, 2L,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalDate.now(),
                " ",
                5L
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenClinicIdIsNull() {
        assertThatThrownBy(() -> new CreateAppointmentCommand(
                1L, 2L,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalDate.now(),
                "Mañana",
                null
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenClinicIdIsZeroOrNegative() {
        assertThatThrownBy(() -> new CreateAppointmentCommand(
                1L, 2L,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalDate.now(),
                "Mañana",
                0L
        )).isInstanceOf(IllegalArgumentException.class);
    }



}