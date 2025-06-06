package ba.unsa.etf.nwt.inventra.inventory_service.messaging.publisher;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.LowStockNotificationDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.dto.ExpiryDateNotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "inventra.exchange";

    public void sendLowStockNotification(String name, int quantity, Long id, String category) {
        LowStockNotificationDTO notification = new LowStockNotificationDTO(id, name, category, quantity);

        rabbitTemplate.convertAndSend(EXCHANGE, "notification.lowstock", notification);
    }

    public void sendExpiryDateNotification(String name, String expiryDate, Long id, String category) {
        ExpiryDateNotificationDTO notification = new ExpiryDateNotificationDTO(name, expiryDate, id, category);

        rabbitTemplate.convertAndSend(EXCHANGE, "notification.expirydate", notification);
    }
}
