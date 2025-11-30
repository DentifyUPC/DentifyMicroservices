package com.upc.dentify.paymentservice.domain.services;

import com.upc.dentify.paymentservice.domain.model.aggregates.Payment;
import com.upc.dentify.paymentservice.domain.model.command.CreatePaymentCommand;
import com.upc.dentify.paymentservice.domain.model.command.ProcessPaymentCommand;
import com.upc.dentify.paymentservice.domain.model.command.UpdatePaymentAmountCommand;

import java.util.Optional;

public interface PaymentCommandService {
    Optional<Payment> handle(CreatePaymentCommand command);
    Optional<Payment> handle(UpdatePaymentAmountCommand command);
    Optional<Payment> handle(ProcessPaymentCommand command);
}
