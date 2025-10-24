package com.upc.dentify.iam.messaging;

import com.upc.dentify.iam.config.RabbitConfig;
import com.upc.dentify.iam.domain.events.UserCreatedEvent;
import com.upc.dentify.iam.domain.events.UserUpdatedEvent;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

//@Component
//@RequiredArgsConstructor
//public class UserEventPublisher {
//
//    private final RabbitTemplate rabbitTemplate;
//
//    public void publishUserCreatedEvent(UserCreatedEvent event) {
//        rabbitTemplate.convertAndSend(
//                RabbitConfig.EXCHANGE,
//                RabbitConfig.USER_CREATED_ROUTING_KEY,
//                event
//        );
//    }
//}


@Component
public class UserCreatedEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public UserCreatedEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserCreated(UserCreatedDomainEvent event) {
        UserCreatedEvent payload = event.getPayload();
        // añade headers/metadata si quieres
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE,
                RabbitConfig.USER_CREATED_ROUTING_KEY,
                payload,
                message -> {
                    message.getMessageProperties().setHeader("correlationId", payload.getUserId());
                    // persistente
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                });
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserUpdated(UserUpdatedDomainEvent event) {
        UserUpdatedEvent payload = event.getPayload();
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE,
                RabbitConfig.USER_UPDATED_ROUTING_KEY,
                payload,
                message -> {
                    message.getMessageProperties().setHeader("correlationId", payload.getUserId());
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                });
    }
}
