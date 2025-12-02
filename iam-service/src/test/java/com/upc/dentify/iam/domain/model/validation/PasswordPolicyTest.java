package com.upc.dentify.iam.domain.model.validation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PasswordPolicyTest {

    @Test
    void validPassword_shouldReturnNoViolations() {
        String pwd = "StrongPassword1!";

        List<String> violations = PasswordPolicy.validate(pwd);

        assertThat(violations).isEmpty();
    }

    @Test
    void invalidPassword_shouldReturnViolations() {
        String pwd = "abc";

        List<String> violations = PasswordPolicy.validate(pwd);

        assertThat(violations).isNotEmpty();
        assertThat(violations.size()).isGreaterThan(1);
    }

    @Test
    void nullPassword_shouldFail() {
        List<String> violations = PasswordPolicy.validate(null);

        assertThat(violations).contains("Password is required.");
    }
}
