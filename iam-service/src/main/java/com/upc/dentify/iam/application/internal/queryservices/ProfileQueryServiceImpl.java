package com.upc.dentify.iam.application.internal.queryservices;

import com.upc.dentify.iam.domain.model.aggregates.User;
import com.upc.dentify.iam.domain.model.queries.GetUserInformationQuery;
import com.upc.dentify.iam.domain.services.ProfileQueryService;
import com.upc.dentify.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.upc.dentify.iam.infrastructure.security.AuthenticatedUserProvider;
import com.upc.dentify.iam.interfaces.rest.resources.PersonalInformationResponseResource;
import org.springframework.stereotype.Service;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {
    private final UserRepository userRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public ProfileQueryServiceImpl(UserRepository userRepository, AuthenticatedUserProvider authenticatedUserProvider) {
        this.userRepository = userRepository;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }


    @Override
    public PersonalInformationResponseResource handle(GetUserInformationQuery query) {
        Long userId = authenticatedUserProvider.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalStateException("User not found"));

        String fullName = user.getPersonName().getFullName();

        return new PersonalInformationResponseResource(
                user.getId(), fullName, user.getUsername(), user.getRole().getId(), user.getClinicId()
        );

    }

}