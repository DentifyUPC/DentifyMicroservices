package com.upc.dentify.iam.application.internal.commandservices;

import com.upc.dentify.iam.domain.events.UserUpdatedEvent;
import com.upc.dentify.iam.domain.model.aggregates.User;
import com.upc.dentify.iam.domain.model.commands.UpdatePasswordCommand;
import com.upc.dentify.iam.domain.model.commands.UpdatePersonalInformationCommand;
import com.upc.dentify.iam.domain.model.valueobjects.PersonName;
import com.upc.dentify.iam.domain.services.ProfileCommandService;
import com.upc.dentify.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.upc.dentify.iam.infrastructure.security.AuthenticatedUserProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final UserRepository userRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public ProfileCommandServiceImpl(UserRepository userRepository, AuthenticatedUserProvider authenticatedUserProvider, BCryptPasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.authenticatedUserProvider = authenticatedUserProvider;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public Optional<User> handle(UpdatePersonalInformationCommand command) {
        Long userId = authenticatedUserProvider.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalStateException("User not found"));
        user.setPersonName(new PersonName(command.firstName(), command.lastName()));

        eventPublisher.publishEvent(new UserUpdatedEvent(
                user.getId(),
                user.getPersonName().firstName(),
                user.getPersonName().lastName(),
                user.getEmail().email(),
                user.getBirthDate().birthDate()
        ));

        try {
            userRepository.save(user);
            return Optional.of(user);
        } catch (Exception e) {
            throw new IllegalStateException("An error occurred while updating user personal information", e);
        }
    }

    @Override
    public Optional<User> handle(UpdatePasswordCommand command) {
        Long userId = authenticatedUserProvider.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalStateException("User not found"));
        System.out.println(user.getPassword() + "\n");

        if (!passwordEncoder.matches(command.oldPassword(), user.getPassword())) {
            throw new IllegalStateException("Old password is incorrect");
        }

        String hashedPassword = passwordEncoder.encode(command.newPassword());
        user.setPassword(hashedPassword);

        try {
            var updatedUser = userRepository.save(user);
            return Optional.of(updatedUser);

        } catch (Exception e) {
            throw new IllegalStateException("An error occurred while updating password", e);
        }
    }
}

