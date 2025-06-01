package ba.unsa.etf.nwt.events;

import ba.unsa.etf.nwt.EventAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderChangedEvent {
    private Long id;
    private LocalDate orderDate;
    private OrderStatus status;
    private List<OrderArticle> orderArticles;
    private EventAction eventAction;
}
