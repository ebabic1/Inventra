package ba.unsa.etf.nwt.inventra.notification_service.listener;

import ba.unsa.etf.nwt.inventra.notification_service.config.RabbitConfig;
import ba.unsa.etf.nwt.inventra.notification_service.dto.LowStockNotificationDTO;
import ba.unsa.etf.nwt.inventra.notification_service.service.EmailService;
import ba.unsa.etf.nwt.inventra.notification_service.service.UsersService;
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
    private final UsersService userService;

    @RabbitListener(queues = RabbitConfig.LOW_STOCK_QUEUE)
    public void onLowStockNotification(LowStockNotificationDTO notification) {
        log.info("Received low stock notification: {}", notification);
        try {

//            TODO: Fix
//            List<String> emails = userService.getAllUserEmails();
//            if (emails.isEmpty()) {
//                log.warn("No user emails found for notification.");
//                return;
//            }

            List<String> emails = Arrays.asList("azametica1@etf.unsa.ba");

            emailService.sendEmailToUsers(
                    emails,
                    "Low Stock Alert: " + notification.getArticleName(),
                    "The article '" + notification.getArticleName() +
                            "' is running low on stock. Current quantity: " +
                            notification.getQuantity()
            );
        } catch (Exception e) {
            log.error("Error processing low stock notification: {}", e.getMessage(), e);
        }
    }
}