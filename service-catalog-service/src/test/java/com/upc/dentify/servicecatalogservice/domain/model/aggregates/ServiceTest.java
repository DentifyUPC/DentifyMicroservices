package com.upc.dentify.servicecatalogservice.domain.model.aggregates;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Domain Test - Service Aggregate")
class ServiceTest {

    @Test
    void constructorShouldSetName() {
        Service service = new Service("Cleaning");
        assertThat(service.getName()).isEqualTo("Cleaning");
    }

    @Test
    void shouldAllowBlankNameBecauseNoValidationExists() {
        Service service = new Service(" ");
        assertThat(service.getName()).isEqualTo(" ");
    }

    @Test
    void shouldAllowNullNameBecauseNoValidationExists() {
        Service service = new Service(null);
        assertThat(service.getName()).isNull();
    }
}
