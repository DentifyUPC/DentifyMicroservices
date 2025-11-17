package com.upc.dentify.clinicmanagementservice.application.internal.commandservices;

import com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl.ExternalItemPerServiceService;
import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ServicesPerClinics;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateServicePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateServicePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ItemPerClinicRepository;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ServicePerClinicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicePerClinicCommandServiceImplTest {

    @Mock
    private ServicePerClinicRepository servicePerClinicRepository;

    @Mock
    private ExternalItemPerServiceService externalItemPerServiceService;

    @Mock
    private ItemPerClinicRepository itemPerClinicRepository;

    @InjectMocks
    private ServicePerClinicCommandServiceImpl service;

    private CreateServicePerClinicCommand createCmd(Long clinicId, Long serviceId, double totalLaborPrice) {
        return new CreateServicePerClinicCommand(clinicId, serviceId, totalLaborPrice);
    }

    private UpdateServicePerClinicCommand updateCmd(Long id, double totalLaborPrice) {
        return new UpdateServicePerClinicCommand(id, totalLaborPrice);
    }


    @Test
    void handle_createSuccess_returnsSavedServicePerClinic() {
        CreateServicePerClinicCommand cmd = createCmd(3L, 11L, 50.0);

        com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ItemRequiredResource requiredItem =
                mock(com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ItemRequiredResource.class);
        when(requiredItem.id()).thenReturn(77L);
        when(requiredItem.quantityRequired()).thenReturn(1L);

        doReturn(List.of(requiredItem)).when(externalItemPerServiceService).getItemsIdsByServiceId(11L);

        com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ItemPerClinic clinicItem =
                mock(com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ItemPerClinic.class);
        when(clinicItem.getItemId()).thenReturn(77L);
        when(clinicItem.getAvailableStock()).thenReturn(10L);
        when(clinicItem.getPrice()).thenReturn(5.0);

        doReturn(List.of(clinicItem)).when(itemPerClinicRepository).findAllByClinicId(3L);

        when(servicePerClinicRepository.existsByClinicIdAndServiceId(3L, 11L)).thenReturn(false);

        ServicesPerClinics saved = mock(ServicesPerClinics.class);
        when(servicePerClinicRepository.save(any(ServicesPerClinics.class))).thenReturn(saved);

        Optional<ServicesPerClinics> result = service.handle(cmd);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(saved);

        verify(servicePerClinicRepository).save(any(ServicesPerClinics.class));
        verify(externalItemPerServiceService).getItemsIdsByServiceId(11L);
        verify(itemPerClinicRepository).findAllByClinicId(3L);
        verify(servicePerClinicRepository).existsByClinicIdAndServiceId(3L, 11L);
    }


    @Test
    void handle_createAlreadyExists_throwsIllegalArgumentException() {
        CreateServicePerClinicCommand cmd = createCmd(3L, 11L, 50.0);

        when(servicePerClinicRepository.existsByClinicIdAndServiceId(3L, 11L)).thenReturn(true);

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(servicePerClinicRepository).existsByClinicIdAndServiceId(3L, 11L);
        verifyNoMoreInteractions(externalItemPerServiceService, itemPerClinicRepository);
    }

    @Test
    void handle_updateSuccess_updatesAndReturns() {
        long id = 1L;
        UpdateServicePerClinicCommand cmd = updateCmd(id, 70.0);

        ServicesPerClinics existing = mock(ServicesPerClinics.class);
        when(servicePerClinicRepository.findById(id)).thenReturn(Optional.of(existing));
        when(servicePerClinicRepository.save(existing)).thenReturn(existing);

        Optional<ServicesPerClinics> result = service.handle(cmd);

        assertThat(result).isPresent();
        verify(existing).updateTotals(70.0);
        verify(servicePerClinicRepository).save(existing);
    }

    @Test
    void handle_updateNotFound_throwsNoSuchElementException() {
        long id = 999L;
        UpdateServicePerClinicCommand cmd = updateCmd(id, 70.0);

        when(servicePerClinicRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(NoSuchElementException.class);

        verify(servicePerClinicRepository).findById(id);
    }

    @Test
    void handle_updateSaveThrows_wrapsInIllegalArgumentException() {
        long id = 2L;
        UpdateServicePerClinicCommand cmd = updateCmd(id, 70.0);

        ServicesPerClinics existing = mock(ServicesPerClinics.class);
        when(servicePerClinicRepository.findById(id)).thenReturn(Optional.of(existing));
        when(servicePerClinicRepository.save(existing)).thenThrow(new RuntimeException("db down"));

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("An error occurred while updating service per clinic");

        verify(existing).updateTotals(70.0);
        verify(servicePerClinicRepository).save(existing);
    }


    private interface RequiredItem {
        Long id();
        int quantityRequired();
    }

    private interface ItemPerClinic {
        Long getItemId();
        Long getAvailableStock();
        Double getPrice();
    }
}
