package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Prescription;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePrescriptionCommand;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PrescriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionCommandServiceImplTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @InjectMocks
    private PrescriptionCommandServiceImpl service;

    private UpdatePrescriptionCommand cmd(long id, String effects) {
        return new UpdatePrescriptionCommand(id, effects);
    }

    @Test
    void handle_updateSuccess_returnsUpdatedPrescription() {
        long id = 1L;
        String newEffects = "Take twice a day";

        UpdatePrescriptionCommand command = cmd(id, newEffects);

        Prescription existing = mock(Prescription.class);

        when(prescriptionRepository.findById(id)).thenReturn(Optional.of(existing));
        when(prescriptionRepository.save(any(Prescription.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Prescription> result = service.handle(command);

        assertThat(result).isPresent();

        verify(existing).setEffects(newEffects);

        verify(prescriptionRepository, times(2)).findById(id);
        verify(prescriptionRepository).save(any(Prescription.class));
    }

    @Test
    void handle_notFound_throwsIllegalArgumentException() {
        long id = 99L;
        UpdatePrescriptionCommand command = cmd(id, "anything");

        when(prescriptionRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Prescription with Id: " + id + " not found");

        verify(prescriptionRepository).findById(id);
        verifyNoMoreInteractions(prescriptionRepository);
    }

    @Test
    void handle_saveThrows_wrapsInIllegalArgumentException() {
        long id = 2L;
        String effects = "effect";
        UpdatePrescriptionCommand command = cmd(id, effects);

        Prescription existing = mock(Prescription.class);

        when(prescriptionRepository.findById(id)).thenReturn(Optional.of(existing));
        when(prescriptionRepository.save(any(Prescription.class))).thenThrow(new RuntimeException("db down"));

        assertThatThrownBy(() -> service.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("An error occurred while updating prescription");

        verify(prescriptionRepository, times(2)).findById(id);
        verify(prescriptionRepository).save(any(Prescription.class));
    }
}
