package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ClinicalRecordEntries;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateClinicalRecordEntryCommand;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.ClinicalRecordEntryRepository;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.ClinicalRecordRepository;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PrescriptionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicalRecordEntriesCommandServiceImplTest {

    @Mock
    private ClinicalRecordEntryRepository clinicalRecordEntryRepository;

    @InjectMocks
    private ClinicalRecordEntriesCommandServiceImpl service;


    @Test
    void handle_updateSuccess_returnsUpdatedEntry() {
        UpdateClinicalRecordEntryCommand cmd =
                new UpdateClinicalRecordEntryCommand(1L, "New evolution", "New obs");

        ClinicalRecordEntries existing = mock(ClinicalRecordEntries.class);

        when(clinicalRecordEntryRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(clinicalRecordEntryRepository.save(existing))
                .thenAnswer(inv -> inv.getArgument(0));

        Optional<ClinicalRecordEntries> result = service.handle(cmd);

        assertThat(result).isPresent();

        InOrder inOrder = inOrder(existing);
        inOrder.verify(existing).setEvolution("New evolution");
        inOrder.verify(existing).setObservation("New obs");

        verify(clinicalRecordEntryRepository).save(existing);
    }

    @Test
    void handle_notFound_throwsIllegalArgumentException() {
        UpdateClinicalRecordEntryCommand cmd =
                new UpdateClinicalRecordEntryCommand(999L, "evo", "obs");

        when(clinicalRecordEntryRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Clinical record entry with id 999 not found");

        verify(clinicalRecordEntryRepository).findById(999L);
        verifyNoMoreInteractions(clinicalRecordEntryRepository);
    }

    @Test
    void handle_saveThrows_wrapsInIllegalArgumentException() {
        UpdateClinicalRecordEntryCommand cmd =
                new UpdateClinicalRecordEntryCommand(1L, "evo", "obs");

        ClinicalRecordEntries existing = mock(ClinicalRecordEntries.class);

        when(clinicalRecordEntryRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(clinicalRecordEntryRepository.save(existing))
                .thenThrow(new RuntimeException("db down"));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("An error occurred while updating Clinical record entry");

        verify(clinicalRecordEntryRepository).save(existing);
    }
}
