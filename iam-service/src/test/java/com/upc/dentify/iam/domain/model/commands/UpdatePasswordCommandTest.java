package com.upc.dentify.iam.domain.model.commands;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UpdatePasswordCommandTest {

    @Test
    void validCommand_shouldCreateSuccessfully() {
        UpdatePasswordCommand cmd =
                new UpdatePasswordCommand("oldPass", "newPass123!", "newPass123!");

        assertThat(cmd.oldPassword()).isEqualTo("oldPass");
        assertThat(cmd.newPassword()).isEqualTo("newPass123!");
    }

    @Test
    void newPasswordAndConfirmDoNotMatch_shouldThrow() {
        assertThatThrownBy(() ->
                new UpdatePasswordCommand("old", "a", "b")
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("do not match");
    }

    @Test
    void emptyOldPassword_shouldThrow() {
        assertThatThrownBy(() ->
                new UpdatePasswordCommand("", "x", "x")
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
