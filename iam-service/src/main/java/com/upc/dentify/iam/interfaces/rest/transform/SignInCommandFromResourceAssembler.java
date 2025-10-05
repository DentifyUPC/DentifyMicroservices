package com.upc.dentify.iam.interfaces.rest.transform;

import com.upc.dentify.iam.domain.model.commands.SignInCommand;
import com.upc.dentify.iam.interfaces.rest.resources.AuthRequestResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(AuthRequestResource resource) {
        return new SignInCommand(resource.username(), resource.password());
    }
}
