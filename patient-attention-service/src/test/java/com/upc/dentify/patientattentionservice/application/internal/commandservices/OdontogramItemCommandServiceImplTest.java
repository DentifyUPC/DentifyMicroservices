package com.upc.dentify.patientattentionservice.application.internal.commandservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.OdontogramItem;
import com.upc.dentify.patientattentionservice.domain.model.aggregates.ToothStatus;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateOdontogramItemCommand;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.OdontogramItemRepository;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.ToothStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OdontogramItemCommandServiceImplTest {

    @Mock
    private OdontogramItemRepository odontogramItemRepository;

    @Mock
    private ToothStatusRepository toothStatusRepository;

    @InjectMocks
    private OdontogramItemCommandServiceImpl service;

    private UpdateOdontogramItemCommand command(long id, long toothStatusId) {
        return new UpdateOdontogramItemCommand(id, toothStatusId);
    }

    @Test
    void handle_updateSuccess_returnsUpdated() {
        long itemId = 1L;
        long toothStatusId = 77L;

        UpdateOdontogramItemCommand cmd = command(itemId, toothStatusId);

        OdontogramItem existing = new OdontogramItem();
        existing.setId(itemId);
        existing.setToothStatus(new ToothStatus(0L));

        when(odontogramItemRepository.findById(itemId)).thenReturn(Optional.of(existing));
        when(toothStatusRepository.existsById(toothStatusId)).thenReturn(true);

        when(odontogramItemRepository.save(any(OdontogramItem.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Optional<OdontogramItem> result = service.handle(cmd);

        assertThat(result).isPresent();

        OdontogramItem saved = result.get();

        assertThat(saved.getToothStatus()).isNotNull();
        assertThat(saved.getToothStatus().getId()).isEqualTo(toothStatusId);

        verify(odontogramItemRepository, times(2)).findById(itemId);
        verify(odontogramItemRepository).save(any(OdontogramItem.class));
    }



    @Test
    void handle_itemNotFound_throwsIllegalArgumentException() {
        long itemId = 99L;
        UpdateOdontogramItemCommand cmd = command(itemId, 1L);

        when(odontogramItemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Odontogram item with id " + itemId + " does not exist");

        verify(odontogramItemRepository).findById(itemId);
        verifyNoMoreInteractions(odontogramItemRepository);
    }

    @Test
    void handle_toothStatusNotExist_throwsIllegalArgumentException() {
        long itemId = 1L;
        long toothStatusId = 55L;
        UpdateOdontogramItemCommand cmd = command(itemId, toothStatusId);

        OdontogramItem existing = mock(OdontogramItem.class);

        when(odontogramItemRepository.findById(itemId)).thenReturn(Optional.of(existing));
        when(toothStatusRepository.existsById(toothStatusId)).thenReturn(false);

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Tooth status with id " + toothStatusId + " does not exist");

        verify(odontogramItemRepository, atLeastOnce()).findById(itemId);
        verify(toothStatusRepository).existsById(toothStatusId);

        verify(odontogramItemRepository, never()).save(any(OdontogramItem.class));

        verifyNoMoreInteractions(odontogramItemRepository, toothStatusRepository);
    }


    @Test
    void handle_saveThrows_wrapsInIllegalArgumentException() {
        long itemId = 1L;
        long toothStatusId = 77L;
        UpdateOdontogramItemCommand cmd = command(itemId, toothStatusId);

        OdontogramItem existing = mock(OdontogramItem.class);

        when(odontogramItemRepository.findById(itemId)).thenReturn(Optional.of(existing));
        when(toothStatusRepository.existsById(toothStatusId)).thenReturn(true);

        when(odontogramItemRepository.save(any(OdontogramItem.class)))
                .thenThrow(new RuntimeException("db down"));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Could not update odontogram item");

        verify(odontogramItemRepository, times(2)).findById(itemId);
        verify(odontogramItemRepository).save(any(OdontogramItem.class));
    }
}
