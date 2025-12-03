package com.upc.dentify.servicecatalogservice.application.internal.queryservices;

import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllItemsRequiredByServiceIdQuery;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.ItemPerServiceRepository;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ItemRequiredResource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Application Test - ItemPerServiceQueryServiceImpl")
class ItemPerServiceQueryServiceImplTest {

    @Mock
    private ItemPerServiceRepository repository;

    @InjectMocks
    private ItemPerServiceQueryServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_returnsRequiredItems() {
        List<ItemRequiredResource> mockList = List.of(
                new ItemRequiredResource(1L, "Gloves", "UNIT", 3L),
                new ItemRequiredResource(2L, "Anesthetics", "ML", 1L)
        );

        when(repository.findAllItemsByServiceId(10L)).thenReturn(mockList);

        List<ItemRequiredResource> result =
                service.handle(new GetAllItemsRequiredByServiceIdQuery(10L));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Gloves");
        assertThat(result.get(1).quantityRequired()).isEqualTo(1L);

        verify(repository).findAllItemsByServiceId(10L);
    }

    @Test
    void handle_emptyList() {
        when(repository.findAllItemsByServiceId(5L)).thenReturn(List.of());

        List<ItemRequiredResource> result =
                service.handle(new GetAllItemsRequiredByServiceIdQuery(5L));

        assertThat(result).isEmpty();
        verify(repository).findAllItemsByServiceId(5L);
    }

    @Test
    void handle_repositoryThrows_shouldPropagate() {
        when(repository.findAllItemsByServiceId(anyLong()))
                .thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() ->
                service.handle(new GetAllItemsRequiredByServiceIdQuery(3L))
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB error");

        verify(repository).findAllItemsByServiceId(3L);
    }
}
