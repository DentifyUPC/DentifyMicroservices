package com.upc.dentify.appointmentservice.application.internal.commandservices;

import com.upc.dentify.appointmentservice.application.internal.outboundservices.ExternalClinicService;
import com.upc.dentify.appointmentservice.application.internal.outboundservices.ExternalOdontologistService;
import com.upc.dentify.appointmentservice.application.internal.outboundservices.ExternalPatientService;
import com.upc.dentify.appointmentservice.application.internal.outboundservices.ExternalShiftService;
import com.upc.dentify.appointmentservice.domain.model.aggregates.Appointment;
import com.upc.dentify.appointmentservice.domain.model.command.CreateAppointmentCommand;
import com.upc.dentify.appointmentservice.domain.model.command.UpdateAppointmentCommand;
import com.upc.dentify.appointmentservice.domain.model.valueobjects.State;
import com.upc.dentify.appointmentservice.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import com.upc.dentify.appointmentservice.interfaces.rest.dtos.PatientExternalResource;
import com.upc.dentify.appointmentservice.interfaces.rest.dtos.OdontologistExternalResource;
import com.upc.dentify.appointmentservice.interfaces.rest.dtos.ShiftResource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AppointmentCommandServiceImplTest {

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private ExternalOdontologistService externalOdontologistService;
    @Mock private ExternalShiftService externalShiftService;
    @Mock private ExternalPatientService externalPatientService;
    @Mock private ExternalClinicService externalClinicService;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AppointmentCommandServiceImpl service;

    private CreateAppointmentCommand validCommandFuture() {
        return new CreateAppointmentCommand(
                11L, // patientId
                22L, // odontologistId
                LocalTime.of(9,0),
                LocalTime.of(10,0),
                LocalDate.now().plusDays(2),
                "MORNING",
                5L // clinicId
        );
    }

    private PatientExternalResource patientDto(long id) {
        return new PatientExternalResource(id, "First", "Last");
    }

    private OdontologistExternalResource odontologistDto(long id) {
        return new OdontologistExternalResource(id, "DocFirst", "DocLast", "MORNING");
    }

    private ShiftResource shiftDto(Long id, LocalTime start, LocalTime end, String name, Long clinicId) {
        return new ShiftResource(id, start, end, name, clinicId);
    }

    @BeforeEach
    void setup() {
        // nothing here, mocks are injected by MockitoExtension
    }

    @Test
    void handle_createSuccess_savesAndPublishesEvent() {
        CreateAppointmentCommand cmd = validCommandFuture();

        when(externalClinicService.existsByClinicId(cmd.clinicId())).thenReturn(true);
        when(externalPatientService.getPatientsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(patientDto(cmd.patientId())));
        when(externalOdontologistService.getAllOdontologistsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(odontologistDto(cmd.odontologistId())));
        when(externalShiftService.getAllShiftsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(shiftDto(1L, LocalTime.of(8,0), LocalTime.of(12,0), "MORNING", 1L)));
        when(appointmentRepository.findAllByOdontologistId(cmd.odontologistId()))
                .thenReturn(List.of());

        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(inv -> {
            Appointment ap = inv.getArgument(0);
            ap.setId(99L);
            return ap;
        });

        Optional<Appointment> result = service.handle(cmd);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(99L);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));

        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        Object published = eventCaptor.getValue();
        assertThat(published).isNotNull();
        assertThat(published).isInstanceOf(com.upc.dentify.appointmentservice.messaging.AppointmentCreatedDomainEvent.class);

    /*
    AppointmentCreatedDomainEvent domainEvent = (AppointmentCreatedDomainEvent) published;
    assertThat(domainEvent.getPayload()).isNotNull();
    assertThat(domainEvent.getPayload().getId()).isEqualTo(99L);
    assertThat(domainEvent.getPayload().getPatientId()).isEqualTo(cmd.patientId());
    */
    }

    @Test
    void handle_clinicNotFound_throwsIllegalArgumentException() {
        CreateAppointmentCommand cmd = validCommandFuture();
        when(externalClinicService.existsByClinicId(cmd.clinicId())).thenReturn(false);

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Clinic not found");

        verifyNoInteractions(appointmentRepository);
    }

    @Test
    void handle_patientNotInClinic_throwsIllegalArgumentException() {
        CreateAppointmentCommand cmd = validCommandFuture();
        when(externalClinicService.existsByClinicId(cmd.clinicId())).thenReturn(true);
        when(externalPatientService.getPatientsByClinicId(cmd.clinicId())).thenReturn(List.of()); // empty

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Patient not found");

        verifyNoInteractions(appointmentRepository);
    }

    @Test
    void handle_odontologistNotInClinic_throwsIllegalArgumentException() {
        CreateAppointmentCommand cmd = validCommandFuture();
        when(externalClinicService.existsByClinicId(cmd.clinicId())).thenReturn(true);
        when(externalPatientService.getPatientsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(patientDto(cmd.patientId())));
        when(externalOdontologistService.getAllOdontologistsByClinicId(cmd.clinicId()))
                .thenReturn(List.of()); // empty

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Odontologist not found");

        verifyNoInteractions(appointmentRepository);
    }

    @Test
    void handle_shiftNotFound_throwsIllegalArgumentException() {
        CreateAppointmentCommand cmd = validCommandFuture();
        when(externalClinicService.existsByClinicId(cmd.clinicId())).thenReturn(true);
        when(externalPatientService.getPatientsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(patientDto(cmd.patientId())));
        when(externalOdontologistService.getAllOdontologistsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(odontologistDto(cmd.odontologistId())));
        // shifts do NOT include the requested shift name ("MORNING"), so return an AFTERNOON shift
        when(externalShiftService.getAllShiftsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(shiftDto(99L, LocalTime.of(13, 0), LocalTime.of(17, 0), "AFTERNOON", 1L)));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Shift not found");
    }


    @Test
    void handle_overlappingAppointments_throwsIllegalArgumentException() {
        CreateAppointmentCommand cmd = validCommandFuture();
        when(externalClinicService.existsByClinicId(cmd.clinicId())).thenReturn(true);
        when(externalPatientService.getPatientsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(patientDto(cmd.patientId())));
        when(externalOdontologistService.getAllOdontologistsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(odontologistDto(cmd.odontologistId())));
        when(externalShiftService.getAllShiftsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(shiftDto(1L, LocalTime.of(8,0), LocalTime.of(12,0), "MORNING", 1L)));

        // existing appointment overlapping the requested 9:00-10:00
        Appointment existing = new Appointment(new CreateAppointmentCommand(
                cmd.patientId(), cmd.odontologistId(),
                LocalTime.of(9,30), LocalTime.of(10,30),
                cmd.appointmentDate(), "MORNING", cmd.clinicId()
        ));
        when(appointmentRepository.findAllByOdontologistId(cmd.odontologistId()))
                .thenReturn(List.of(existing));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Overlapping appointments");
    }

    @Test
    void handle_exceedsSixHours_throwsIllegalArgumentException() {
        CreateAppointmentCommand cmd = validCommandFuture();
        when(externalClinicService.existsByClinicId(cmd.clinicId())).thenReturn(true);
        when(externalPatientService.getPatientsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(patientDto(cmd.patientId())));
        when(externalOdontologistService.getAllOdontologistsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(odontologistDto(cmd.odontologistId())));
        // Shift extended until 18:00 so that all existing appointments fit inside shift bounds
        when(externalShiftService.getAllShiftsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(shiftDto(1L, LocalTime.of(8,0), LocalTime.of(18,0), "MORNING", 1L)));

        // create existing same-day appointments summing 5.5 hours
        Appointment a1 = new Appointment(new CreateAppointmentCommand(
                100L, cmd.odontologistId(), LocalTime.of(8,0), LocalTime.of(10,0),
                cmd.appointmentDate(), "MORNING", cmd.clinicId()
        )); // 2.0 h

        Appointment a2 = new Appointment(new CreateAppointmentCommand(
                101L, cmd.odontologistId(), LocalTime.of(10,0), LocalTime.of(12,0),
                cmd.appointmentDate(), "MORNING", cmd.clinicId()
        )); // 2.0 h

        Appointment a3 = new Appointment(new CreateAppointmentCommand(
                102L, cmd.odontologistId(), LocalTime.of(12,30), LocalTime.of(14,0),
                cmd.appointmentDate(), "MORNING", cmd.clinicId()
        )); // 1.5 h

        // total existing = 2 + 2 + 1.5 = 5.5 h
        when(appointmentRepository.findAllByOdontologistId(cmd.odontologistId()))
                .thenReturn(List.of(a1, a2, a3));

        // New appointment is 1 hour -> total 6.5 > 6 -> should throw
        CreateAppointmentCommand bigCmd = new CreateAppointmentCommand(
                cmd.patientId(), cmd.odontologistId(), LocalTime.of(15,0), LocalTime.of(16,0),
                cmd.appointmentDate(), "MORNING", cmd.clinicId()
        );

        assertThatThrownBy(() -> service.handle(bigCmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("maximum of 6 hours");
    }


    @Test
    void handle_dateInPast_throwsIllegalArgumentException() {
        CreateAppointmentCommand cmd = new CreateAppointmentCommand(
                1L, 2L,
                LocalTime.of(9,0), LocalTime.of(10,0),
                LocalDate.now().minusDays(1),
                "MORNING", 5L
        );
        when(externalClinicService.existsByClinicId(cmd.clinicId())).thenReturn(true);
        when(externalPatientService.getPatientsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(patientDto(cmd.patientId())));
        when(externalOdontologistService.getAllOdontologistsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(odontologistDto(cmd.odontologistId())));
        when(externalShiftService.getAllShiftsByClinicId(cmd.clinicId()))
                .thenReturn(List.of(shiftDto(1L, LocalTime.of(8,0), LocalTime.of(12,0), "MORNING", 1L)));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Appointment date cannot be in the past");
    }

    // ----- Update tests -----

    @Test
    void handle_updateNotFound_throwsIllegalArgumentException() {
        UpdateAppointmentCommand upd = new UpdateAppointmentCommand(999L, "CANCELLED");
        when(appointmentRepository.findById(upd.id())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(upd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Appointment not found");
    }

    @Test
    void handle_updateSuccess_savesAndReturnsUpdated() {
        UpdateAppointmentCommand upd = new UpdateAppointmentCommand(1L, "ABSENT");

        Appointment existing = new Appointment();
        existing.setId(1L);
        existing.setState(State.PENDING);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Appointment> result = service.handle(upd);

        assertThat(result).isPresent();
        assertThat(result.get().getState()).isEqualTo(State.ABSENT);

        verify(appointmentRepository, times(2)).findById(1L);
        verify(appointmentRepository).save(any(Appointment.class));
    }


    @Test
    void handle_updateSaveThrows_wrapsInIllegalArgumentException() {
        UpdateAppointmentCommand upd = new UpdateAppointmentCommand(1L, "ABSENT");

        Appointment existing = new Appointment();
        existing.setId(1L);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(existing));

        when(appointmentRepository.save(any(Appointment.class)))
                .thenThrow(new RuntimeException("db down"));

        assertThatThrownBy(() -> service.handle(upd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("An error occurred while updating appointment");

        verify(appointmentRepository, times(2)).findById(1L);
        verify(appointmentRepository).save(any(Appointment.class));
    }

}
