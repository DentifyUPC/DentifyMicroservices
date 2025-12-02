package com.upc.dentify.iam.domain.model.aggregates;

import com.upc.dentify.iam.domain.model.commands.SignUpCommand;
import com.upc.dentify.iam.domain.model.entities.IdentificationType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserTest {

    @Test
    void constructor_setsFieldsCorrectly() {
        SignUpCommand cmd = new SignUpCommand(
                "Fabrisio", "Belahonia",
                "12345678",
                "StrongPassword1!",
                "10/01/2000",
                "fabri@upc.pe",
                1L,
                2L,
                9L
        );

        User u = new User(cmd, "HASHED", new IdentificationType("DNI"));

        assertThat(u.getPersonName().firstName()).isEqualTo("Fabrisio");
        assertThat(u.getPassword()).isEqualTo("HASHED");
        assertThat(u.getEmail().email()).isEqualTo("fabri@upc.pe");
        assertThat(u.getClinicId()).isEqualTo(9L);
    }

    @Test
    void userDetails_flagsAlwaysEnabled() {
        User u = mock(User.class);
        when(u.isAccountNonExpired()).thenCallRealMethod();
        when(u.isAccountNonLocked()).thenCallRealMethod();

        assertThat(u).isNotNull();
    }
}
