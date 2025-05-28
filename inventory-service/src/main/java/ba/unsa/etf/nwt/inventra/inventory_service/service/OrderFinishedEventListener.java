package ba.unsa.etf.nwt.inventra.inventory_service.service;

import ba.unsa.etf.nwt.inventra.order_service.event.OrderFinishedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFinishedEventListener {

    private final ArticleService articleService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "order.finished")
    public void onOrderFinished(OrderFinishedEvent event) {
        try {
            articleService.updateArticleStocks(event.getOrderedArticles());
        } catch (Exception e) {
            event.setRollback(true);
            rabbitTemplate.convertAndSend("inventra.exchange", "order.rollback", event);
        }
    }
}
