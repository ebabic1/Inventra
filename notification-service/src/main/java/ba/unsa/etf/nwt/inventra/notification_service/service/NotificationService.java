package ba.unsa.etf.nwt.inventra.notification_service.service;

import ba.unsa.etf.nwt.inventra.notification_service.model.Notification;
import ba.unsa.etf.nwt.inventra.notification_service.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void createAndSendNotification(String title, String message, Long userId) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUserId(userId);
        notification.setTimestamp(System.currentTimeMillis());

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }
}
