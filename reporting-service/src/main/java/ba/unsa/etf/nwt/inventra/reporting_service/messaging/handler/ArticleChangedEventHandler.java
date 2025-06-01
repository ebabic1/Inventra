package ba.unsa.etf.nwt.inventra.reporting_service.messaging.handler;

import ba.unsa.etf.nwt.events.ArticleChangedEvent;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Article;
import ba.unsa.etf.nwt.inventra.reporting_service.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleChangedEventHandler {
    private final ArticleService articleService;

    public void handle(ArticleChangedEvent articleChangedEvent) {
        log.info("Handling ArticleChangedEvent for articleId={}", articleChangedEvent.getId());
        switch (articleChangedEvent.getAction()) {
            case CREATED:
                articleService.create(givenArticle(articleChangedEvent));
                break;
            case UPDATED:
                articleService.update(articleChangedEvent.getId(), givenArticle(articleChangedEvent));
                break;
            case DELETED:
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
        article.setCategory(articleChangedEvent.getCategory());
        return article;
    }
}
