package ba.unsa.etf.nwt.inventra.notification_service.controller;
import ba.unsa.etf.nwt.inventra.notification_service.model.Notification;
import ba.unsa.etf.nwt.inventra.notification_service.service.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    // ToDo: Update
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.findAll();
    }
}
