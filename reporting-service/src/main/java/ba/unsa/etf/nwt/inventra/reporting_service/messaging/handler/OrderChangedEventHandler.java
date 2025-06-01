package ba.unsa.etf.nwt.inventra.reporting_service.messaging.handler;

import ba.unsa.etf.nwt.EventAction;
import ba.unsa.etf.nwt.events.ArticleChangedEvent;
import ba.unsa.etf.nwt.events.OrderChangedEvent;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Article;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Order;
import ba.unsa.etf.nwt.inventra.reporting_service.model.OrderArticle;
import ba.unsa.etf.nwt.inventra.reporting_service.model.OrderStatus;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.ArticleRepository;
import ba.unsa.etf.nwt.inventra.reporting_service.service.ArticleService;
import ba.unsa.etf.nwt.inventra.reporting_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderChangedEventHandler {

    private final OrderService orderService;
    private final ArticleRepository articleRepository;

    public void handle(OrderChangedEvent orderChangedEvent) {
        log.info("Handling OrderChangedEvent for orderId={}", orderChangedEvent.getId());
        switch (orderChangedEvent.getEventAction()) {
            case EventAction.CREATED:
                orderService.create(givenOrder(orderChangedEvent));
                break;
            case EventAction.UPDATED:
                orderService.update(orderChangedEvent.getId(), givenOrder(orderChangedEvent));
                break;
            case EventAction.DELETED:
                orderService.delete(orderChangedEvent.getId());
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + orderChangedEvent.getEventAction());
        }
    }

    private Order givenOrder(OrderChangedEvent orderChangedEvent) {
        Order order = new Order();
        order.setId(orderChangedEvent.getId());
        order.setOrderDate(orderChangedEvent.getOrderDate().atStartOfDay());
        order.setStatus(OrderStatus.valueOf(String.valueOf(orderChangedEvent.getStatus())));
        List<OrderArticle> orderArticles = orderChangedEvent.getOrderArticles().stream().map(dto -> {
            Article article = articleRepository.findById(dto.getArticleId())
                    .orElseGet(() -> {
                        Article newArticle = new Article();
                        newArticle.setId(dto.getArticleId());
                        newArticle.setName(dto.getArticleName());
                        newArticle.setPrice(dto.getArticlePrice());
                        newArticle.setCategory(dto.getArticleCategory());
                        return articleRepository.save(newArticle);
                    });

            OrderArticle orderArticle = new OrderArticle();
            orderArticle.setArticle(article);
            orderArticle.setQuantity(dto.getQuantity());
            orderArticle.setOrder(order);
            return orderArticle;
        }).toList();
        order.setOrderArticles(orderArticles);
        return order;
    }
}
