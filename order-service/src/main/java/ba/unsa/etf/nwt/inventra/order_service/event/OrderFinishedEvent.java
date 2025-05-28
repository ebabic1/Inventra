package ba.unsa.etf.nwt.inventra.order_service.event;

import ba.unsa.etf.nwt.inventra.order_service.dto.OrderArticleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderFinishedEvent {
    private Long orderId;
    private List<OrderArticleDTO> orderedArticles;
    private boolean rollback;
}

