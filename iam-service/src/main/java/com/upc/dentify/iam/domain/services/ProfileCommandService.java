package com.upc.dentify.iam.domain.services;

import com.upc.dentify.iam.domain.model.aggregates.User;
import com.upc.dentify.iam.domain.model.commands.UpdatePasswordCommand;
import com.upc.dentify.iam.domain.model.commands.UpdatePersonalInformationCommand;

import java.util.Optional;

public interface ProfileCommandService {
    Optional<User> handle(UpdatePasswordCommand command);
    Optional<User> handle(UpdatePersonalInformationCommand command);
}
