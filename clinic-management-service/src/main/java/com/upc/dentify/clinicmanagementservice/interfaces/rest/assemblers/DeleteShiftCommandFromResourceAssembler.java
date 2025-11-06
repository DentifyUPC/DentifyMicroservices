package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.DeleteShiftCommand;

public class DeleteShiftCommandFromResourceAssembler {

    public static DeleteShiftCommand toCommandFromResource(Long id) {
        return new DeleteShiftCommand(id);
    }

}
