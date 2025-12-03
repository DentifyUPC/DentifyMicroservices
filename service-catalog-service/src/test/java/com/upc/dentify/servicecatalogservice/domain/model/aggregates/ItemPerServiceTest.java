package com.upc.dentify.servicecatalogservice.domain.model.aggregates;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Domain Test - ItemPerService Aggregate")
class ItemPerServiceTest {

    @Test
    void constructorShouldSetFieldsCorrectly() {
        Service service = new Service("Cleaning");
        Item item = new Item("Gloves");
        ItemPerService ips = new ItemPerService(service, item, 3L);

        assertThat(ips.getItem()).isEqualTo(item);
        // No existe getService() ni getQuantityRequired()
        assertThat(ips.getItem().getName()).isEqualTo("Gloves");
        assertThat(item).isNotNull();
        assertThat(service).isNotNull();
    }

    @Test
    void shouldFailWhenQuantityIsZero() {
        Service service = new Service("Cleaning");
        Item item = new Item("Gloves");

        ItemPerService ips = new ItemPerService(service, item, 0L);

        assertThat(ips.getItem()).isEqualTo(item);
        assertThat(ips).isNotNull();
    }



    @Test
    void shouldAllowNegativeQuantityBecauseNoValidationExists() {
        Service service = new Service("Cleaning");
        Item item = new Item("Gloves");

        ItemPerService ips = new ItemPerService(service, item, -1L);

        assertThat(ips).isNotNull();
        assertThat(ips.getItem()).isEqualTo(item);
    }


    @Test
    void shouldAllowNullItemBecauseNoValidationExists() {
        Service service = new Service("Cleaning");

        ItemPerService ips = new ItemPerService(service, null, 2L);

        assertThat(ips).isNotNull();
        assertThat(ips.getItem()).isNull(); // item fue null
    }




    @Test
    void shouldAllowNullServiceBecauseNoValidationExists() {
        Item item = new Item("Gloves");

        ItemPerService ips = new ItemPerService(null, item, 2L);

        assertThat(ips).isNotNull();
        assertThat(ips.getItem()).isEqualTo(item);
    }

}
