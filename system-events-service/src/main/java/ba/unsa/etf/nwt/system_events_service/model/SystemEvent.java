package ba.unsa.etf.nwt.system_events_service.model;

import ba.unsa.etf.nwt.system_events_service.ActionType;
import ba.unsa.etf.nwt.system_events_service.ResponseType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant timestamp;

    private String microserviceName;

    private String username;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private String resourceName;

    @Enumerated(EnumType.STRING)
    private ResponseType responseType;
}
