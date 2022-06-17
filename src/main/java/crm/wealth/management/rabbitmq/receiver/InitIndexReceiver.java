package crm.wealth.management.rabbitmq.receiver;

import crm.wealth.management.rabbitmq.InitIndexMessage;
import crm.wealth.management.rabbitmq.RabbitConfig;
import crm.wealth.management.service.requestsearch.service.InitIndexService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitIndexReceiver {

    protected final Log logger = LogFactory.getLog(this.getClass());


    @Autowired
    private InitIndexService initIndexService;


    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void initIndexRequest(InitIndexMessage message) throws Exception {

           if(message == null) {
               logger.error("Cannot receive message");
               throw new Exception("Cannot receive message");
           }

           try {
               initIndexService.createIndex(message.getRequest());
           }catch (Exception e) {
               logger.error("Error processing init index message", e);
           }
    }
}
