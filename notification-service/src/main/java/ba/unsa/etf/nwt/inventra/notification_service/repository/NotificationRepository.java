package ba.unsa.etf.nwt.inventra.notification_service.repository;

import ba.unsa.etf.nwt.inventra.notification_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}
