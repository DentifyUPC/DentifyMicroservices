package com.upc.dentify.paymentservice.interfaces.rest.assemblers;

import com.upc.dentify.paymentservice.domain.model.command.CreatePaymentCommand;
import com.upc.dentify.paymentservice.interfaces.rest.dtos.CreatePaymentRequest;


public class CreatePaymentCommandFromResourceAssembler {
    

    public static CreatePaymentCommand fromResourceToCommand(CreatePaymentRequest resource) {
        return new CreatePaymentCommand(
                resource.patientId(),
                resource.appointmentId(),
                0.0
        );
    }
}
