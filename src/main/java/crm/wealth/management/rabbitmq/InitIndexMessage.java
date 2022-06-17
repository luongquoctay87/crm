package crm.wealth.management.rabbitmq;

import crm.wealth.management.model.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitIndexMessage {

    private Request request;
}
