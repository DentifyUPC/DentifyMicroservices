package com.upc.dentify.iam.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.iam.domain.model.aggregates.User;
import com.upc.dentify.iam.domain.model.valueobjects.Username;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    @Test
    void findByUsername_mockExample() {
        UserRepository repo = mock(UserRepository.class);
        Username u = new Username("12345678");

        when(repo.findByUsername(u)).thenReturn(Optional.of(mock(User.class)));

        assertThat(repo.findByUsername(u)).isPresent();
    }
}
