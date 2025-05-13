package ba.unsa.etf.nwt.system_events_service.repository;

import ba.unsa.etf.nwt.system_events_service.model.SystemEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemEventsRepository extends JpaRepository<SystemEvent, Long> {
}
