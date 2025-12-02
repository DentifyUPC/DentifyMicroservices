package com.upc.dentify.iam.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BirthDateTest {

    @Test
    void validBirthDate_shouldPass() {
        BirthDate b = new BirthDate("10/10/2000");

        assertThat(b.birthDate()).isEqualTo("10/10/2000");
    }

    @Test
    void nullBirthdate_shouldThrow() {
        assertThatThrownBy(() -> new BirthDate(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalidFormat_shouldThrow() {
        assertThatThrownBy(() -> new BirthDate("10-10-2000"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
