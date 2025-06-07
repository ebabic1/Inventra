package ba.unsa.etf.nwt.inventra.notification_service.listener;

import ba.unsa.etf.nwt.inventra.notification_service.client.AuthClient;
import ba.unsa.etf.nwt.inventra.notification_service.config.RabbitConfig;
import ba.unsa.etf.nwt.inventra.notification_service.dto.LowStockNotificationDTO;
import ba.unsa.etf.nwt.inventra.notification_service.service.EmailService;
import ba.unsa.etf.nwt.inventra.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LowStockNotificationListener {

    private final EmailService emailService;
    private final AuthClient authClient;
    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitConfig.LOW_STOCK_QUEUE)
    public void onLowStockNotification(LowStockNotificationDTO notification) {
        log.info("Received low stock notification: {}", notification);

        try {
            List<String> emails = authClient.getAllUserEmails();
            if (emails.isEmpty()) {
                log.warn("No user emails found for notification.");
                return;
            }

            emailService.sendEmailToUsers(
                    emails,
                    "Low Stock Alert: " + notification.getName(),
                    "The article '" + notification.getName() +
                            "' (ID: " + notification.getId() + ", Category: " + notification.getCategory() +
                            ") is running low on stock. Current quantity: " +
                            notification.getQuantity()
            );

            notificationService.createAndSendNotification(
                    "Low Stock Alert",
                    "The article '" + notification.getName() + "' is running low on stock. Current quantity: " + notification.getQuantity(),
                    null
            );

        } catch (Exception e) {
            log.error("Error processing low stock notification: {}", e.getMessage(), e);
        }
    }
}