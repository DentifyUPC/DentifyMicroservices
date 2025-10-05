package com.upc.dentify.iam.domain.services;

import com.upc.dentify.iam.domain.model.commands.SignInCommand;
import com.upc.dentify.iam.domain.model.commands.SignUpCommand;
import com.upc.dentify.iam.interfaces.rest.resources.AuthResponseResource;
import com.upc.dentify.iam.interfaces.rest.resources.RegisterResponseResource;

public interface AuthCommandService {

    AuthResponseResource login(SignInCommand command);
    RegisterResponseResource register(SignUpCommand command);

}
