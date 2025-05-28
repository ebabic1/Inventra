package ba.unsa.etf.nwt.inventra.order_service.event;

import ba.unsa.etf.nwt.inventra.order_service.event.OrderFinishedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishOrderFinishedEvent(OrderFinishedEvent event) {
        rabbitTemplate.convertAndSend("inventra.exchange", "order.finished", event);
    }
}
