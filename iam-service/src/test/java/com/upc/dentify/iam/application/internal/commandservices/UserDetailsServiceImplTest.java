package com.upc.dentify.iam.application.internal.commandservices;

import com.upc.dentify.iam.domain.model.aggregates.User;
import com.upc.dentify.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl service;

    @Test
    void loadUser_success() {
        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(mock(User.class)));

        assertThat(service.loadUserByUsername("12345678"))
                .isNotNull();
    }

    @Test
    void loadUser_notFound_throws() {
        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("abc"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
