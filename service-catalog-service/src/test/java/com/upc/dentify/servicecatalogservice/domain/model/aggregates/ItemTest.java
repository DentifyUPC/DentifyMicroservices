package com.upc.dentify.servicecatalogservice.domain.model.aggregates;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Domain Test - Item Aggregate")
class ItemTest {

    @Test
    void shouldAllowBlankNameBecauseNoValidationExists() {
        Item item = new Item(" ");
        assertThat(item).isNotNull();
        assertThat(item.getName()).isEqualTo(" ");
    }

    @Test
    void shouldAllowNullNameBecauseNoValidationExists() {
        Item item = new Item((String) null);
        assertThat(item).isNotNull();
        assertThat(item.getName()).isNull();
    }

    @Test
    void shouldAllowNullUnitTypeWhenConstructorDoesNotRequireIt() {
        Item item = new Item("ValidName");
        assertThat(item.getUnitType()).isNull();
    }
}
