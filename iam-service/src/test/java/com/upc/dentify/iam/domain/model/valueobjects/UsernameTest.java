package com.upc.dentify.iam.domain.model.valueobjects;

import com.upc.dentify.iam.domain.model.entities.IdentificationType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UsernameTest {

    @Test
    void validDniUsername_shouldPass() {
        IdentificationType dni = new IdentificationType("DNI");

        Username u = new Username("12345678", dni);

        assertThat(u.getValue()).isEqualTo("12345678");
    }

    @Test
    void invalidDni_shouldThrow() {
        IdentificationType dni = new IdentificationType("DNI");

        assertThatThrownBy(() -> new Username("1234", dni))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("8 digits");
    }

    @Test
    void validForeigner_shouldPass() {
        IdentificationType fi = new IdentificationType("FOREIGNER ID CARD");

        Username u = new Username("123456789012", fi);
        assertThat(u.getValue()).isEqualTo("123456789012");
    }

    @Test
    void unsupportedType_shouldThrow() {
        IdentificationType custom = new IdentificationType("PASSPORT");

        assertThatThrownBy(() -> new Username("ABC123", custom))
                .hasMessageContaining("Unsupported");
    }
}
