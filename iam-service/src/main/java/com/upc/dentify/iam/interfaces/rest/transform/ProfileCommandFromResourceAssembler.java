package com.upc.dentify.iam.interfaces.rest.transform;

import com.upc.dentify.iam.domain.model.commands.UpdatePasswordCommand;
import com.upc.dentify.iam.domain.model.commands.UpdatePersonalInformationCommand;
import com.upc.dentify.iam.interfaces.rest.resources.UpdatePasswordRequestResource;
import com.upc.dentify.iam.interfaces.rest.resources.UpdatePersonalInformationRequestResource;

public class ProfileCommandFromResourceAssembler {
    public static UpdatePersonalInformationCommand toCommand(UpdatePersonalInformationRequestResource request) {
        return new UpdatePersonalInformationCommand(
                request.firstName(),
                request.lastName()
        );
    }

    public static UpdatePasswordCommand toCommand(UpdatePasswordRequestResource request) {
        return new UpdatePasswordCommand(
                request.oldPassword(),
                request.confirmNewPassword(),
                request.newPassword()
                );
    }
}
