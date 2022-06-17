package crm.wealth.management.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(MqMessage mqMessage) {
         rabbitTemplate.convertAndSend(mqMessage.getExchange(), mqMessage.getRoutingKey(), mqMessage.getMessage());
    }
}

