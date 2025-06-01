package ba.unsa.etf.nwt.inventra.order_service.messaging.publisher;

import ba.unsa.etf.nwt.EventAction;
import ba.unsa.etf.nwt.events.OrderArticle;
import ba.unsa.etf.nwt.events.OrderChangedEvent;
import ba.unsa.etf.nwt.events.OrderStatus;
import ba.unsa.etf.nwt.inventra.order_service.messaging.event.OrderFinishedEvent;
import ba.unsa.etf.nwt.inventra.order_service.model.Article;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "inventra.exchange";

    public void publishOrderFinishedEvent(OrderFinishedEvent event) {
        rabbitTemplate.convertAndSend("inventra.exchange", "order.finished", event);
    }

    public void publishOrderChangedEvent(Order order, EventAction action) {
        OrderChangedEvent event = new OrderChangedEvent();
        event.setId(order.getId());
        event.setOrderDate(order.getOrderDate());
        event.setStatus(OrderStatus.valueOf(order.getStatus().name()));
        event.setEventAction(action);

        List<OrderArticle> articles = order.getOrderArticles().stream().map(oa -> {
            Article article = oa.getArticle();
            OrderArticle dto = new OrderArticle();
            dto.setArticleId(article.getId());
            dto.setArticleName(article.getName());
            dto.setArticlePrice(article.getPrice());
            dto.setQuantity(oa.getQuantity());
            return dto;
        }).toList();

        event.setOrderArticles(articles);

        String routingKey = switch (action) {
            case CREATED -> "order.created";
            case UPDATED -> "order.updated";
            case DELETED -> "order.deleted";
        };

        rabbitTemplate.convertAndSend(EXCHANGE, routingKey, event);
    }
}
