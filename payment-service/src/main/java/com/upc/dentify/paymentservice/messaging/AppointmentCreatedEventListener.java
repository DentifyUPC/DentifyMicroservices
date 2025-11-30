package com.upc.dentify.paymentservice.messaging;

import com.upc.dentify.paymentservice.config.RabbitConfig;
import com.upc.dentify.paymentservice.domain.events.AppointmentCreatedEvent;
import com.upc.dentify.paymentservice.domain.model.command.CreatePaymentCommand;
import com.upc.dentify.paymentservice.domain.services.PaymentCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentCreatedEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentCreatedEventListener.class);
    private final PaymentCommandService paymentCommandService;

    public AppointmentCreatedEventListener(PaymentCommandService paymentCommandService) {
        this.paymentCommandService = paymentCommandService;
    }

    @RabbitListener(queues = RabbitConfig.APPOINTMENT_CREATED_QUEUE)
    public void handleAppointmentCreated(AppointmentCreatedEvent event) {
        logger.info("=== Received appointment created event ===");
        logger.info("Event object: {}", event);
        
        if (event != null) {
            logger.info("appointmentId={}, patientId={}, odontologistId={}", 
                        event.getId(), event.getPatientId(), event.getOdontologistId());
        } else {
            logger.error("Event is null!");
            return;
        }
        
        try {
            logger.info("Creating payment command for appointment: {}", event.getId());
            
            // Create payment with amount 0.0, admin will update it later
            CreatePaymentCommand command = new CreatePaymentCommand(
                event.getPatientId(),
                event.getId(),  // appointmentId
                0.0             // Initial amount is 0.0, admin updates it
            );
            
            logger.info("Payment command created: patientId={}, appointmentId={}, amount=0.0", 
                       event.getPatientId(), event.getId());
            
            var payment = paymentCommandService.handle(command);
            
            if (payment.isPresent()) {
                logger.info("✓ Payment created successfully for appointment: {} with id: {}", 
                           event.getId(), payment.get().getId());
            } else {
                logger.error("✗ Failed to create payment for appointment: {}", event.getId());
            }
        } catch (Exception e) {
            logger.error("✗ Error creating payment for appointment: {}", event.getId(), e);
            throw e;
        }
    }
}
