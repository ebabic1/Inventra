package ba.unsa.etf.nwt.inventra.reporting_service.messaging.listener;

import ba.unsa.etf.nwt.events.OrderChangedEvent;
import ba.unsa.etf.nwt.inventra.reporting_service.messaging.config.RabbitConfig;
import ba.unsa.etf.nwt.inventra.reporting_service.messaging.handler.OrderChangedEventHandler;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderChangedEventListener {
    private final OrderChangedEventHandler handler;

    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
    public void onOrderChanged(OrderChangedEvent event) {
        try {
            log.info("Received OrderChangedEvent: {}", event);
            handler.handle(event);
        } catch (Exception e) {
            log.error("Failed to process OrderChangedEvent", e);
        }
    }
}
