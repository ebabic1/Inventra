package ba.unsa.etf.nwt.inventra.inventory_service.messaging.publisher;

import ba.unsa.etf.nwt.EventAction;
import ba.unsa.etf.nwt.events.ArticleChangedEvent;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "inventra.exchange";

    public void publish(Article article, EventAction action) {
        ArticleChangedEvent event = new ArticleChangedEvent(
                article.getId(),
                article.getName(),
                article.getPrice(),
                article.getCategory(),
                article.getQuantity(),
                action
        );

        rabbitTemplate.convertAndSend(EXCHANGE, "article." + action.getKey(), event);
    }
}
