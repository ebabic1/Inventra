package ba.unsa.etf.nwt.inventra.order_service.messaging.listener;

import ba.unsa.etf.nwt.events.ArticleChangedEvent;
import ba.unsa.etf.nwt.inventra.order_service.messaging.config.RabbitConfig;
import ba.unsa.etf.nwt.inventra.order_service.messaging.handler.ArticleChangedEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleEventListener {

    private final ArticleChangedEventHandler handler;

    @RabbitListener(queues = RabbitConfig.ARTICLE_QUEUE)
    public void onArticleChanged(ArticleChangedEvent event)  {
        try {
            log.info("Received ArticleChangedEvent: {}", event);
            handler.handle(event);
        } catch (Exception e) {
            log.error("Failed to process ArticleChangedEvent", e);
        }
    }
}
