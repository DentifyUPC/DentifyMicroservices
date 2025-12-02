package com.upc.dentify.iam.application.internal.commandservices;

import com.upc.dentify.iam.domain.model.aggregates.User;
import com.upc.dentify.iam.domain.model.commands.UpdatePasswordCommand;
import com.upc.dentify.iam.domain.model.commands.UpdatePersonalInformationCommand;
import com.upc.dentify.iam.domain.model.entities.Role;
import com.upc.dentify.iam.domain.model.valueobjects.PersonName;
import com.upc.dentify.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.upc.dentify.iam.infrastructure.security.AuthenticatedUserProvider;
import com.upc.dentify.iam.messaging.UserUpdatedDomainEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileCommandServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private AuthenticatedUserProvider authProvider;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private ApplicationEventPublisher publisher;

    @InjectMocks
    private ProfileCommandServiceImpl service;

    @Test
    void updatePersonalInformation_success() {
        UpdatePersonalInformationCommand cmd =
                new UpdatePersonalInformationCommand("Nuevo", "Apellido");

        User mockUser = mock(User.class);
        when(authProvider.getCurrentUserId()).thenReturn(50L);
        when(userRepository.findById(50L)).thenReturn(Optional.of(mockUser));

        Role role = new Role("ADMIN");  //
        when(mockUser.getRole()).thenReturn(role);

        when(userRepository.save(mockUser)).thenReturn(mockUser);

        Optional<User> result = service.handle(cmd);

        assertThat(result).isPresent();
        verify(publisher, times(1))
                .publishEvent(any(UserUpdatedDomainEvent.class));
    }

    @Test
    void updatePersonalInformation_userNotFound_throws() {
        when(authProvider.getCurrentUserId()).thenReturn(10L);
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(new UpdatePersonalInformationCommand("A","B")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void updatePassword_success() {
        UpdatePasswordCommand cmd =
                new UpdatePasswordCommand("oldPass", "newPass123!", "newPass123!");

        User user = mock(User.class);
        when(authProvider.getCurrentUserId()).thenReturn(77L);
        when(userRepository.findById(77L)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("oldPass", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPass123!")).thenReturn("HASHED");
        when(userRepository.save(user)).thenReturn(user);

        Optional<User> resp = service.handle(cmd);

        assertThat(resp).isPresent();
        verify(user).setPassword("HASHED");
    }

    @Test
    void updatePassword_wrongOldPassword_throws() {
        UpdatePasswordCommand cmd =
                new UpdatePasswordCommand("X", "newY!", "newY!");

        User user = mock(User.class);
        when(authProvider.getCurrentUserId()).thenReturn(77L);
        when(userRepository.findById(77L)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("X", user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> service.handle(cmd))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Old password is incorrect");
    }

}
