package com.upc.dentify.servicecatalogservice.application.internal.queryservices;

import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Item;
import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllItemsQuery;
import com.upc.dentify.servicecatalogservice.domain.model.queries.GetItemByIdQuery;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.ItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Application Test - ItemQueryServiceImpl")
class ItemQueryServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemQueryServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_getAllItems_returnsList() {
        List<Item> items = List.of(new Item("Gloves"), new Item("Anesthetics"));

        when(itemRepository.findAll()).thenReturn(items);

        List<Item> result = service.handle(new GetAllItemsQuery());

        assertThat(result).hasSize(2);
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void handle_getAllItems_returnsEmptyList() {
        when(itemRepository.findAll()).thenReturn(List.of());

        List<Item> result = service.handle(new GetAllItemsQuery());

        assertThat(result).isEmpty();
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void handle_getById_found_returnsItem() {
        Item item = new Item("Gloves");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Optional<Item> result = service.handle(new GetItemByIdQuery(1L));

        assertThat(result).isPresent();
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void handle_getById_notFound_returnsEmptyOptional() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Item> result = service.handle(new GetItemByIdQuery(99L));

        assertThat(result).isEmpty();
        verify(itemRepository, times(1)).findById(99L);
    }

    @Test
    void handle_getById_repositoryThrows_shouldPropagateException() {
        when(itemRepository.findById(anyLong()))
                .thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> service.handle(new GetItemByIdQuery(10L)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB error");

        verify(itemRepository).findById(10L);
    }
}
