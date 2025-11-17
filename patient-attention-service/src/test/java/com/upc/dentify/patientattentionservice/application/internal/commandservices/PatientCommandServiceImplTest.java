package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.*;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePatientCommand;
import com.upc.dentify.patientattentionservice.domain.model.events.UserCreatedEvent;
import com.upc.dentify.patientattentionservice.domain.model.events.UserUpdatedEvent;
import com.upc.dentify.patientattentionservice.domain.model.valueobjects.Address;
import com.upc.dentify.patientattentionservice.domain.model.valueobjects.Gender;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientCommandServiceImplTest {

    @Mock private PatientRepository patientRepository;
    @Mock private AnamnesisRepository anamnesisRepository;
    @Mock private OdontogramRepository odontogramRepository;
    @Mock private OdontogramItemRepository odontogramItemRepository;
    @Mock private TeethRepository teethRepository;
    @Mock private ToothStatusRepository toothStatusRepository;
    @Mock private ClinicalRecordRepository clinicalRecordRepository;

    @InjectMocks
    private PatientCommandServiceImpl service;


    @Test
    void handle_updatePatient_returnsUpdatedWhenExists() {
        UpdatePatientCommand cmd = mock(UpdatePatientCommand.class);
        when(cmd.patientId()).thenReturn(10L);
        when(cmd.gender()).thenReturn(Gender.FEMALE);
        when(cmd.street()).thenReturn("Main St");
        when(cmd.district()).thenReturn("D");
        when(cmd.department()).thenReturn("Dep");
        when(cmd.province()).thenReturn("Prov");
        when(cmd.phoneNumber()).thenReturn("956236123");

        Patient existing = mock(Patient.class);
        when(patientRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(patientRepository.save(existing)).thenAnswer(inv -> inv.getArgument(0));

        Optional<Patient> result = service.handle(cmd);

        assertThat(result).isPresent();
        verify(existing).updateAdditionalInfo(eq(Gender.FEMALE), any(Address.class), eq("956236123"));
        verify(patientRepository).save(existing);
    }

    @Test
    void handle_updatePatient_returnsEmptyWhenNotFound() {
        UpdatePatientCommand cmd = mock(UpdatePatientCommand.class);
        when(cmd.patientId()).thenReturn(999L);

        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Patient> result = service.handle(cmd);

        assertThat(result).isEmpty();
        verify(patientRepository).findById(999L);
        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    void handle_userCreated_ignoresWhenPatientAlreadyExists() {
        UserCreatedEvent event = mock(UserCreatedEvent.class);
        when(event.getUserId()).thenReturn(55L);

        when(patientRepository.existsByUserId(55L)).thenReturn(true);

        service.handle(event);

        verify(patientRepository).existsByUserId(55L);
        verifyNoMoreInteractions(patientRepository, anamnesisRepository, odontogramRepository,
                odontogramItemRepository, clinicalRecordRepository);
    }

    @Test
    void handle_userCreated_throwsIfRoleNotPatient() {
        UserCreatedEvent event = mock(UserCreatedEvent.class);
        when(event.getUserId()).thenReturn(1L);
        when(event.getRole()).thenReturn(99L); // not 3L

        assertThatThrownBy(() -> service.handle(event))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Role must be 3L: Patient");
    }

    @Test
    void handle_userCreated_createsAllEntitiesAndItems() {
        UserCreatedEvent event = mock(UserCreatedEvent.class);
        when(event.getUserId()).thenReturn(101L);
        when(event.getRole()).thenReturn(3L);
        when(event.getFirstName()).thenReturn("A");
        when(event.getLastName()).thenReturn("B");
        when(event.getBirthDate()).thenReturn("01/11/2004");

        when(event.getEmail()).thenReturn("a@b.com");
        when(event.getClinicId()).thenReturn(5L);

        when(patientRepository.existsByUserId(101L)).thenReturn(false);

        Patient savedPatient = mock(Patient.class);
        when(savedPatient.getId()).thenReturn(200L);
        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        Anamnesis savedAnamnesis = mock(Anamnesis.class);
        when(savedAnamnesis.getId()).thenReturn(300L);
        when(anamnesisRepository.save(any(Anamnesis.class))).thenReturn(savedAnamnesis);

        Odontogram savedOdontogram = mock(Odontogram.class);
        when(savedOdontogram.getId()).thenReturn(400L);
        when(odontogramRepository.save(any(Odontogram.class))).thenReturn(savedOdontogram);

        Teeth t1 = mock(Teeth.class);
        Teeth t2 = mock(Teeth.class);
        when(teethRepository.findAll()).thenReturn(List.of(t1, t2));

        ToothStatus defaultStatus = mock(ToothStatus.class);
        when(toothStatusRepository.findByName("Sano")).thenReturn(Optional.of(defaultStatus));

        @SuppressWarnings("rawtypes")
        ArgumentCaptor<Iterable> itemsCaptor = ArgumentCaptor.forClass(Iterable.class);

        service.handle(event);

        verify(patientRepository).existsByUserId(101L);
        verify(patientRepository).save(any(Patient.class));
        verify(anamnesisRepository).save(any(Anamnesis.class));
        verify(odontogramRepository).save(any(Odontogram.class));

        verify(teethRepository).findAll();
        verify(toothStatusRepository).findByName("Sano");

        verify(odontogramItemRepository).saveAll(itemsCaptor.capture());
        Iterable<OdontogramItem> savedItems = (Iterable<OdontogramItem>) itemsCaptor.getValue();
        assertThat(savedItems).isNotNull();
        int count = 0;
        for (OdontogramItem it : savedItems) {
            count++;
            assertThat(it.getToothStatus()).isEqualTo(defaultStatus);
            assertThat(it.getOdontogram()).isNotNull();
            assertThat(it.getTeeth()).isIn(t1, t2);
        }
        assertThat(count).isEqualTo(2);

        verify(clinicalRecordRepository).save(any(ClinicalRecords.class));
    }

    @Test
    void handle_userUpdated_throwsIfRoleNotPatient() {
        UserUpdatedEvent ev = mock(UserUpdatedEvent.class);
        when(ev.getRole()).thenReturn(2L);

        assertThatThrownBy(() -> service.handle(ev))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Role must be 3L: Patient");
    }

    @Test
    void handle_userUpdated_updatesWhenPatientExists() {
        UserUpdatedEvent ev = mock(UserUpdatedEvent.class);
        when(ev.getUserId()).thenReturn(500L);
        when(ev.getRole()).thenReturn(3L);
        when(ev.getFirstName()).thenReturn("New");
        when(ev.getLastName()).thenReturn("Name");

        Patient p = mock(Patient.class);
        when(patientRepository.findByUserId(500L)).thenReturn(Optional.of(p));
        when(patientRepository.save(any(Patient.class))).thenReturn(p);

        service.handle(ev);

        verify(patientRepository).findByUserId(500L);
        verify(p).updateBasicInfo("New", "Name");
        verify(patientRepository).save(p);
    }

    @Test
    void handle_userUpdated_doesNothingWhenPatientNotFound() {
        UserUpdatedEvent ev = mock(UserUpdatedEvent.class);
        when(ev.getUserId()).thenReturn(501L);
        when(ev.getRole()).thenReturn(3L);

        when(patientRepository.findByUserId(501L)).thenReturn(Optional.empty());

        service.handle(ev);

        verify(patientRepository).findByUserId(501L);
        verifyNoMoreInteractions(patientRepository);
    }
}
