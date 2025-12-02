package com.upc.dentify.iam.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PersonNameTest {

    @Test
    void validName_shouldPass() {
        PersonName name = new PersonName("Katherin", "Silva");

        assertThat(name.getFullName()).isEqualTo("Katherin Silva");
    }

    @Test
    void emptyFirstName_shouldThrow() {
        assertThatThrownBy(() -> new PersonName("", "Silva"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("FirstName");
    }

    @Test
    void emptyLastName_shouldThrow() {
        assertThatThrownBy(() -> new PersonName("Fabri", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("LastName");
    }
}
