package com.upc.dentify.iam.domain.model.commands;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SignInCommandTest {

    @Test
    void validCommand_shouldCreateSuccessfully() {
        SignInCommand cmd = new SignInCommand("username", "secret123");

        assertThat(cmd.username()).isEqualTo("username");
        assertThat(cmd.password()).isEqualTo("secret123");
    }

    @Test
    void emptyUsername_shouldThrow() {
        assertThatThrownBy(() -> new SignInCommand("", "pw"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username cannot be empty");
    }

    @Test
    void emptyPassword_shouldThrow() {
        assertThatThrownBy(() -> new SignInCommand("abc", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Password cannot be empty");
    }
}
