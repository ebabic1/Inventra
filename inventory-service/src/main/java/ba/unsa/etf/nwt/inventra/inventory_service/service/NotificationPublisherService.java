package ba.unsa.etf.nwt.inventra.inventory_service.service;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.LowStockNotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public void sendLowStockNotification(String name, int quantity, Long id, String category) {
        LowStockNotificationDTO notification = new LowStockNotificationDTO(name, quantity, id, category);

        rabbitTemplate.convertAndSend("inventra.exchange", "notification.lowstock", notification);
    }
}
