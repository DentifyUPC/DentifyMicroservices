package com.upc.dentify.servicecatalogservice.application.internal.queryservices;

import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Service;
import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllServicesQuery;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.ServiceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Application Test - ServiceQueryServiceImpl")
class ServiceQueryServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceQueryServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_getAllServices_returnsList() {
        List<Service> services = List.of(
                new Service("Cleaning"),
                new Service("Whitening"));

        when(serviceRepository.findAll()).thenReturn(services);

        List<Service> result = service.handle(new GetAllServicesQuery());

        assertThat(result).hasSize(2);
        verify(serviceRepository, times(1)).findAll();
    }

    @Test
    void handle_getAllServices_returnsEmptyList() {
        when(serviceRepository.findAll()).thenReturn(List.of());

        List<Service> result = service.handle(new GetAllServicesQuery());

        assertThat(result).isEmpty();
        verify(serviceRepository, times(1)).findAll();
    }

    @Test
    void existsById_whenTrue() {
        when(serviceRepository.existsById(1L)).thenReturn(true);

        boolean exists = service.existsById(1L);

        assertThat(exists).isTrue();
        verify(serviceRepository).existsById(1L);
    }

    @Test
    void existsById_whenFalse() {
        when(serviceRepository.existsById(2L)).thenReturn(false);

        boolean exists = service.existsById(2L);

        assertThat(exists).isFalse();
        verify(serviceRepository).existsById(2L);
    }
}
