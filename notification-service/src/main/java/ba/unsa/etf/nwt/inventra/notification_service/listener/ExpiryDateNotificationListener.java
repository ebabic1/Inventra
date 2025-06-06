package ba.unsa.etf.nwt.inventra.notification_service.listener;

import ba.unsa.etf.nwt.inventra.notification_service.client.AuthClient;
import ba.unsa.etf.nwt.inventra.notification_service.config.RabbitConfig;
import ba.unsa.etf.nwt.inventra.notification_service.dto.ExpiryDateNotificationDTO;
import ba.unsa.etf.nwt.inventra.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpiryDateNotificationListener {

    private final EmailService emailService;
    private final AuthClient authClient;

    @RabbitListener(queues = RabbitConfig.EXPIRY_DATE_QUEUE)
    public void onExpiryDateNotification(ExpiryDateNotificationDTO notification) {
        log.info("Received expiry date notification: {}", notification);

        try {
            List<String> emails = authClient.getAllUserEmails();
            if (emails.isEmpty()) {
                log.warn("No user emails found for notification.");
                return;
            }

            emailService.sendEmailToUsers(
                    emails,
                    "Expiry Alert: " + notification.getName(),
                    "The article '" + notification.getName() +
                            "' (ID: " + notification.getId() + ", Category: " + notification.getCategory() +
                            ") is nearing its expiry date. Expiry date: " +
                            notification.getExpiryDate()
            );

        } catch (Exception e) {
            log.error("Error processing expiry date notification: {}", e.getMessage(), e);
        }
    }
}
