package com.upc.dentify.iam.domain.model.commands;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SignUpCommandTest {

    @Test
    void validCommand_shouldCreateSuccessfully() {
        SignUpCommand cmd = new SignUpCommand(
                "Fabrisio", "Belahonia",
                "12345678",
                "StrongPassword1!",
                "10/01/2000",      // ✔️ BirthDate válido
                "fabri@upc.pe",
                1L,
                2L,
                9L
        );

        assertThat(cmd.firstName()).isEqualTo("Fabrisio");
        assertThat(cmd.lastName()).isEqualTo("Belahonia");
        assertThat(cmd.email()).isEqualTo("fabri@upc.pe");
        assertThat(cmd.clinicId()).isEqualTo(9L);
    }

    @Test
    void invalidPassword_shouldFailWithPolicy() {
        assertThatThrownBy(() ->
                new SignUpCommand(
                        "A", "B",
                        "12345678",
                        "weak",               // ❌ violates PasswordPolicy
                        "10/10/2000",
                        "test@upc.pe",
                        1L, 2L, 9L
                )
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Password policy violations");
    }

    @Test
    void nullClinicId_shouldThrow() {
        assertThatThrownBy(() ->
                new SignUpCommand(
                        "A", "B",
                        "12345678",
                        "StrongPassword1!",
                        "10/10/2000",
                        "test@upc.pe",
                        1L, 2L, null
                )
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Clinic Id cannot be null");
    }
}
