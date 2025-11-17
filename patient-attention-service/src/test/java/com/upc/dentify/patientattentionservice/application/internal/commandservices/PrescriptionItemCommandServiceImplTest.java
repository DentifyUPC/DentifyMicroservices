package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.PrescriptionItems;
import com.upc.dentify.patientattentionservice.domain.model.commands.CreatePrescriptionItemCommand;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePrescriptionItemCommand;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PrescriptionItemRepository;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PrescriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionItemCommandServiceImplTest {

    @Mock
    private PrescriptionItemRepository prescriptionItemRepository;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @InjectMocks
    private PrescriptionItemCommandServiceImpl service;

    private CreatePrescriptionItemCommand createCmd(long prescriptionId, String medication, String description) {
        return new CreatePrescriptionItemCommand(medication, description, prescriptionId);
    }

    private UpdatePrescriptionItemCommand updateCmd(long id, String medication, String description) {
        return new UpdatePrescriptionItemCommand(id, medication, description);
    }


    @Test
    void handle_createSuccess_returnsNewItem() {
        CreatePrescriptionItemCommand cmd = createCmd(10L, "Med A", "Desc A");

        when(prescriptionRepository.existsById(10L)).thenReturn(true);
        when(prescriptionItemRepository.save(any(PrescriptionItems.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Optional<PrescriptionItems> result = service.handle(cmd);

        assertThat(result).isPresent();
        PrescriptionItems created = result.get();
        assertThat(created.getMedicationName()).isEqualTo("Med A");
        assertThat(created.getDescription()).isEqualTo("Desc A");

        verify(prescriptionRepository).existsById(10L);
        verify(prescriptionItemRepository).save(any(PrescriptionItems.class));
    }

    @Test
    void handle_createInvalidPrescription_throwsIllegalArgumentException() {
        CreatePrescriptionItemCommand cmd = createCmd(99L, "Med", "Desc");

        when(prescriptionRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid prescription id");

        verify(prescriptionRepository).existsById(99L);
        verifyNoInteractions(prescriptionItemRepository);
    }

    @Test
    void handle_createSaveThrows_wrapsInIllegalArgumentException() {
        CreatePrescriptionItemCommand cmd = createCmd(10L, "M", "D");

        when(prescriptionRepository.existsById(10L)).thenReturn(true);
        when(prescriptionItemRepository.save(any(PrescriptionItems.class)))
                .thenThrow(new RuntimeException("db error"));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Error saving prescription item");

        verify(prescriptionRepository).existsById(10L);
        verify(prescriptionItemRepository).save(any(PrescriptionItems.class));
    }


    @Test
    void handle_updateSuccess_returnsUpdatedItem() {
        long id = 1L;
        UpdatePrescriptionItemCommand cmd = updateCmd(id, "NewMed", "NewDesc");

        PrescriptionItems existing = new PrescriptionItems();
        existing.setId(id);
        existing.setMedicationName("OldMed");
        existing.setDescription("OldDesc");

        when(prescriptionItemRepository.findById(id)).thenReturn(Optional.of(existing));
        when(prescriptionItemRepository.save(any(PrescriptionItems.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<PrescriptionItems> result = service.handle(cmd);

        assertThat(result).isPresent();
        PrescriptionItems updated = result.get();
        assertThat(updated.getMedicationName()).isEqualTo("NewMed");
        assertThat(updated.getDescription()).isEqualTo("NewDesc");

        verify(prescriptionItemRepository, times(2)).findById(id);
        verify(prescriptionItemRepository).save(any(PrescriptionItems.class));
    }

    @Test
    void handle_updateNotFound_throwsIllegalArgumentException() {
        long id = 999L;
        UpdatePrescriptionItemCommand cmd = updateCmd(id, "X", "Y");

        when(prescriptionItemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid prescription item id");

        verify(prescriptionItemRepository).findById(id);
        verifyNoMoreInteractions(prescriptionItemRepository);
    }

    @Test
    void handle_updateSaveThrows_wrapsInIllegalArgumentException() {
        long id = 2L;
        UpdatePrescriptionItemCommand cmd = updateCmd(id, "Med", "Desc");

        PrescriptionItems existing = new PrescriptionItems();
        existing.setId(id);

        when(prescriptionItemRepository.findById(id)).thenReturn(Optional.of(existing));
        when(prescriptionItemRepository.save(any(PrescriptionItems.class)))
                .thenThrow(new RuntimeException("db down"));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Error updating prescription item");

        verify(prescriptionItemRepository, times(2)).findById(id);
        verify(prescriptionItemRepository).save(any(PrescriptionItems.class));
    }
}
