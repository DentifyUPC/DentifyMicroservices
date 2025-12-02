package com.upc.dentify.iam.domain.model.commands;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UpdatePersonalInformationCommandTest {

    @Test
    void validCommand_shouldCreateSuccessfully() {
        UpdatePersonalInformationCommand cmd =
                new UpdatePersonalInformationCommand("Katherin", "Silva");

        assertThat(cmd.firstName()).isEqualTo("Katherin");
        assertThat(cmd.lastName()).isEqualTo("Silva");
    }

    @Test
    void emptyFirstName_shouldThrow() {
        assertThatThrownBy(() ->
                new UpdatePersonalInformationCommand("", "Test")
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("firstName is required");
    }

    @Test
    void emptyLastName_shouldThrow() {
        assertThatThrownBy(() ->
                new UpdatePersonalInformationCommand("Test", "")
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("lastName is required");
    }
}
