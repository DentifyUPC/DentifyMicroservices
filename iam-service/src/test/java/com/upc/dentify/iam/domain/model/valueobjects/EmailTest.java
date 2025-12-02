package com.upc.dentify.iam.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EmailTest {

    @Test
    void validEmail_shouldPass() {
        Email email = new Email("test@example.com");

        assertThat(email.email()).isEqualTo("test@example.com");
    }

    @Test
    void invalidEmail_shouldThrow() {
        assertThatThrownBy(() -> new Email("bad email"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email format");
    }

    @Test
    void nullEmail_shouldThrow() {
        assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
