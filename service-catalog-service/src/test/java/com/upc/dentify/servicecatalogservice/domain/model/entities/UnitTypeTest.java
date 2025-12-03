package com.upc.dentify.servicecatalogservice.domain.model.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Domain Test - UnitType Entity")
class UnitTypeTest {

    @Test
    void constructorShouldSetName() {
        UnitType type = new UnitType("UNIT");
        assertThat(type.getName()).isEqualTo("UNIT");
    }

    @Test
    void shouldAllowNullNameBecauseDomainDoesNotValidate() {
        UnitType type = new UnitType(null);

        assertThat(type).isNotNull();
        assertThat(type.getName()).isNull();
    }

    @Test
    void shouldAllowBlankNameBecauseDomainDoesNotValidate() {
        UnitType type = new UnitType(" ");

        assertThat(type).isNotNull();
        assertThat(type.getName()).isEqualTo(" ");
    }
}
