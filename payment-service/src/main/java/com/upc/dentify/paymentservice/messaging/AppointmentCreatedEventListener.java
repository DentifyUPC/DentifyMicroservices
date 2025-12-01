package com.upc.dentify.paymentservice.messaging;

import com.upc.dentify.paymentservice.application.internal.outboundservices.ExternalServicePerClinicService;
import com.upc.dentify.paymentservice.config.RabbitConfig;
import com.upc.dentify.paymentservice.domain.events.AppointmentCreatedEvent;
import com.upc.dentify.paymentservice.domain.model.command.CreatePaymentCommand;
import com.upc.dentify.paymentservice.domain.services.PaymentCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class AppointmentCreatedEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentCreatedEventListener.class);
    private final PaymentCommandService paymentCommandService;
    private final ExternalServicePerClinicService externalServicePerClinicService;

    public AppointmentCreatedEventListener(PaymentCommandService paymentCommandService, ExternalServicePerClinicService externalServicePerClinicService) {
        this.paymentCommandService = paymentCommandService;
        this.externalServicePerClinicService = externalServicePerClinicService;
    }

    @RabbitListener(queues = RabbitConfig.APPOINTMENT_CREATED_QUEUE)
    public void handleAppointmentCreated(AppointmentCreatedEvent event) {
        logger.info("=== Received appointment created event ===");

        if (event == null || event.getId() == null) {
            logger.error("Event or event.id is null!");
            return;
        }

        logger.info("Event - appointmentId: {}, serviceId: {}, clinicId: {}",
                event.getId(), event.getServiceId(), event.getClinicId());

        try {
            Double totalPrice = 0.0;
            if (event.getServiceId() != null && event.getClinicId() != null) {
                totalPrice = externalServicePerClinicService.getTotalServicePrice(
                        event.getClinicId(),
                        event.getServiceId()
                );
                logger.info("Retrieved service price: {}", totalPrice);
            } else {
                logger.warn("serviceId or clinicId is null");
            }

            CreatePaymentCommand command = new CreatePaymentCommand(
                    event.getPatientId(),
                    event.getId(),
                    totalPrice
            );

            logger.info("Creating payment: patientId={}, appointmentId={}, amount={}",
                    event.getPatientId(), event.getId(), totalPrice);

            var payment = paymentCommandService.handle(command);

            if (payment.isPresent()) {
                logger.info("Payment created with amount: {} for appointment: {}",
                        totalPrice, event.getId());
            } else {
                logger.error("Failed to create payment for appointment: {}", event.getId());
            }
        } catch (Exception e) {
            logger.error("Error creating payment for appointment: {}", event.getId(), e);
            throw e;
        }
    }
}
