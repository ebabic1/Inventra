package ba.unsa.etf.nwt.inventra.inventory_service.service;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.LowStockNotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public void sendLowStockNotification(String articleName, int quantity) {
        LowStockNotificationDTO notification = new LowStockNotificationDTO(articleName, quantity);

        rabbitTemplate.convertAndSend("inventra.exchange", "notification.lowstock", notification);
    }
}
