package com.upc.dentify.iam.application.internal.commandservices;

import com.upc.dentify.iam.application.internal.outboundservices.acl.ExternalClinicService;
import com.upc.dentify.iam.domain.events.UserCreatedEvent;
import com.upc.dentify.iam.domain.model.aggregates.User;
import com.upc.dentify.iam.domain.model.commands.SignInCommand;
import com.upc.dentify.iam.domain.model.commands.SignUpCommand;
import com.upc.dentify.iam.domain.model.entities.IdentificationType;
import com.upc.dentify.iam.domain.model.entities.Role;
import com.upc.dentify.iam.domain.services.JwtService;
import com.upc.dentify.iam.infrastructure.persistence.jpa.repositories.IdentificationTypeRepository;
import com.upc.dentify.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.upc.dentify.iam.interfaces.rest.resources.AuthResponseResource;
import com.upc.dentify.iam.interfaces.rest.resources.RegisterResponseResource;
import com.upc.dentify.iam.messaging.UserCreatedDomainEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthCommandServiceImplTest {

    @Mock private JwtService jwtService;
    @Mock private UserRepository userRepository;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private IdentificationTypeRepository identificationTypeRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private ExternalClinicService externalClinicService;

    @InjectMocks
    private AuthCommandServiceImpl service;

    private SignUpCommand validSignUp() {
        return new SignUpCommand(
                "Katherin", "Silva",
                "12345678",
                "StrongPassword1!",
                "12/10/2004",
                "kat@upc.edu.pe",
                1L,
                2L,
                99L
        );
    }

    private SignInCommand validLogin() {
        return new SignInCommand("12345678", "StrongPassword1!");
    }


    @Test
    void login_success_returnsTokens() {
        SignInCommand cmd = validLogin();

        User mockUser = mock(User.class);
        Role mockRole = mock(Role.class);

        when(mockRole.getId()).thenReturn(2L);
        when(mockUser.getRole()).thenReturn(mockRole);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(mockUser));
        when(mockUser.getPassword()).thenReturn("HASHEDPASS");

        when(passwordEncoder.matches(cmd.password(), "HASHEDPASS"))
                .thenReturn(true);

        when(jwtService.generateAccessToken(mockUser)).thenReturn("access123");
        when(jwtService.generateRefreshToken(mockUser)).thenReturn("refresh123");

        AuthResponseResource response = service.login(cmd);

        assertThat(response.accessToken()).isEqualTo("access123");
        assertThat(response.refreshToken()).isEqualTo("refresh123");
        assertThat(response.roleId()).isEqualTo(2L);   //
    }



    @Test
    void login_userNotFound_throwsUsernameNotFound() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(validLogin()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void login_wrongPassword_throwsBadCredentials() {
        User user = mock(User.class);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThatThrownBy(() -> service.login(validLogin()))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid credentials");
    }


    @Test
    void register_success_savesUserAndPublishesEvent() {
        SignUpCommand cmd = validSignUp();

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(cmd.password())).thenReturn("hashed");
        when(identificationTypeRepository.findById(cmd.identificationTypeId()))
                .thenReturn(Optional.of(new IdentificationType("DNI")));
        when(externalClinicService.existsByClinicId(cmd.clinicId())).thenReturn(true);

        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(88L);
            return u;
        });

        when(jwtService.generateAccessToken(any())).thenReturn("A");
        when(jwtService.generateRefreshToken(any())).thenReturn("R");

        RegisterResponseResource response = service.register(cmd);

        assertThat(response.userId()).isEqualTo(88L);

        verify(eventPublisher, times(1))
                .publishEvent(any(UserCreatedDomainEvent.class));
    }

    @Test
    void register_usernameAlreadyExists_throwsException() {
        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> service.register(validSignUp()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already used");
    }

    @Test
    void register_invalidIdType_throwsException() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(identificationTypeRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.register(validSignUp()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid Identification Type");
    }

    @Test
    void register_clinicNotFound_throwsException() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(identificationTypeRepository.findById(any()))
                .thenReturn(Optional.of(new IdentificationType("DNI")));
        when(passwordEncoder.encode(any())).thenReturn("h");
        when(externalClinicService.existsByClinicId(any())).thenReturn(false);

        assertThatThrownBy(() -> service.register(validSignUp()))
                .hasMessageContaining("Clinic not found");
    }

    @Test
    void register_repositoryFails_wrapsException() {
        SignUpCommand cmd = new SignUpCommand(
                "Katherin", "Silva",
                "12345678",
                "StrongPassword1!",
                "12/10/2004",   //
                "kat@upc.edu.pe",
                1L,
                2L,
                99L
        );

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(any()))
                .thenReturn("hashed");

        when(identificationTypeRepository.findById(any()))
                .thenReturn(Optional.of(new IdentificationType("DNI")));

        when(externalClinicService.existsByClinicId(any()))
                .thenReturn(true);

        when(userRepository.save(any()))
                .thenThrow(new RuntimeException("DB down"));

        assertThatThrownBy(() -> service.register(cmd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("A error occurred while saving user");
    }

}
