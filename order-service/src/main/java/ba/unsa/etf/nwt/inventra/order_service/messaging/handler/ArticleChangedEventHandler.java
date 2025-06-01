package ba.unsa.etf.nwt.inventra.order_service.messaging.handler;

import ba.unsa.etf.nwt.events.ArticleChangedEvent;
import ba.unsa.etf.nwt.inventra.order_service.model.Article;
import ba.unsa.etf.nwt.inventra.order_service.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleChangedEventHandler {

    private final ArticleService articleService;

    public void handle(ArticleChangedEvent articleChangedEvent) {
        switch (articleChangedEvent.getAction()) {
            case CREATED:
                log.info("Handling Article created event for articleId={}", articleChangedEvent.getId());
                articleService.create(givenArticle(articleChangedEvent));
                break;
            case UPDATED:
                log.info("Handling Article updated event for articleId={}", articleChangedEvent.getId());
                articleService.update(articleChangedEvent.getId(), givenArticle(articleChangedEvent));
                break;
            case DELETED:
                log.info("Handling Article deleted event for articleId={}", articleChangedEvent.getId());
                articleService.delete(articleChangedEvent.getId());
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + articleChangedEvent.getAction());
        }
    }

    private Article givenArticle(ArticleChangedEvent articleChangedEvent) {
        Article article = new Article();
        article.setId(articleChangedEvent.getId());
        article.setName(articleChangedEvent.getName());
        article.setPrice(articleChangedEvent.getPrice());
        return article;
    }
}
