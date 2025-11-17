package com.upc.dentify.clinicmanagementservice.application.internal.commandservices;

import com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl.ExternalItemService;
import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ItemPerClinic;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateItemPerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateItemPerClinicCommand;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ItemPerClinicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemPerClinicCommandServiceImplTest {

    @Mock
    private ItemPerClinicRepository itemPerClinicRepository;

    @Mock
    private ExternalItemService externalItemService;

    @InjectMocks
    private ItemPerClinicCommandServiceImpl service;

    private CreateItemPerClinicCommand createCmd(
            Long availableStock,
            Long minimumStock,
            Double price,
            Long itemId,
            Long clinicId
    ) {
        return new CreateItemPerClinicCommand(
                availableStock,
                minimumStock,
                price,
                itemId,
                clinicId
        );
    }


    @Test
    void handle_createSuccess_returnsNewItemPerClinic() {

        CreateItemPerClinicCommand cmd = createCmd(10L, 2L, 5.0, 77L, 3L);

        when(externalItemService.existsById(77L)).thenReturn(true);
        when(itemPerClinicRepository.existsByClinicIdAndItemId(3L, 77L)).thenReturn(false);
        when(itemPerClinicRepository.save(any(ItemPerClinic.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<ItemPerClinic> result = service.handle(cmd);

        assertThat(result).isPresent();
        verify(externalItemService).existsById(77L);
        verify(itemPerClinicRepository).existsByClinicIdAndItemId(3L, 77L);
        verify(itemPerClinicRepository).save(any(ItemPerClinic.class));
    }

    @Test
    void handle_createItemNotInCatalog_throwsIllegalArgumentException() {
        CreateItemPerClinicCommand cmd = createCmd(10L, 2L, 5.0, 77L, 3L);

        when(externalItemService.existsById(77L)).thenReturn(false);

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("This item does not exist in the catalog");

        verify(externalItemService).existsById(77L);
        verifyNoMoreInteractions(itemPerClinicRepository);
    }

    @Test
    void handle_createAlreadyExistsInClinic_throwsIllegalArgumentException() {
        CreateItemPerClinicCommand cmd = createCmd(10L, 2L, 5.0, 77L, 3L);

        when(externalItemService.existsById(77L)).thenReturn(true);
        when(itemPerClinicRepository.existsByClinicIdAndItemId(3L, 77L)).thenReturn(true);

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("This item already exists in the clinic");

        verify(externalItemService).existsById(77L);
        verify(itemPerClinicRepository).existsByClinicIdAndItemId(3L, 77L);
        verify(itemPerClinicRepository, never()).save(any(ItemPerClinic.class));
    }

    @Test
    void handle_createSaveThrows_wrapsInIllegalArgumentException() {
        CreateItemPerClinicCommand cmd = createCmd(10L, 2L, 5.0, 77L, 3L);

        when(externalItemService.existsById(77L)).thenReturn(true);
        when(itemPerClinicRepository.existsByClinicIdAndItemId(3L, 77L)).thenReturn(false);
        when(itemPerClinicRepository.save(any(ItemPerClinic.class))).thenThrow(new RuntimeException("db down"));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Error saving item per clinic");

        verify(itemPerClinicRepository).save(any(ItemPerClinic.class));
    }


    private UpdateItemPerClinicCommand updateCmd(long id, Long newAvailableStock, Long newMinimumStock, Double newPrice) {
        return new UpdateItemPerClinicCommand(id, newAvailableStock, newMinimumStock, newPrice);
    }

    @Test
    void handle_updateSuccess_updatesAndReturns() {
        long id = 1L;
        UpdateItemPerClinicCommand cmd = updateCmd(id, 10L, 2L, 5.0);

        ItemPerClinic existing = mock(ItemPerClinic.class);

        when(itemPerClinicRepository.findById(id)).thenReturn(Optional.of(existing));
        when(itemPerClinicRepository.save(existing)).thenAnswer(inv -> inv.getArgument(0));

        Optional<ItemPerClinic> result = service.handle(cmd);

        assertThat(result).isPresent();
        verify(existing).update(cmd);
        verify(itemPerClinicRepository).save(existing);
    }

    @Test
    void handle_updateNotFound_throwsNoSuchElementException() {
        long id = 999L;
        UpdateItemPerClinicCommand cmd = updateCmd(id, 10L, 2L, 5.0);

        when(itemPerClinicRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(NoSuchElementException.class);

        verify(itemPerClinicRepository).findById(id);
    }

    @Test
    void handle_updateSaveThrows_wrapsInIllegalArgumentException() {
        long id = 5L;
        UpdateItemPerClinicCommand cmd = updateCmd(id, 10L, 2L, 5.0);

        ItemPerClinic existing = mock(ItemPerClinic.class);

        when(itemPerClinicRepository.findById(id)).thenReturn(Optional.of(existing));
        when(itemPerClinicRepository.save(existing)).thenThrow(new RuntimeException("db down"));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("An error occurred while updating item per clinic");

        verify(existing).update(cmd);
        verify(itemPerClinicRepository).save(existing);
    }
}
