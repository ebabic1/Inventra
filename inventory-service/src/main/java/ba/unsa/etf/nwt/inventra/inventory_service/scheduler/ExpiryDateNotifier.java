package ba.unsa.etf.nwt.inventra.inventory_service.scheduler;

import ba.unsa.etf.nwt.inventra.inventory_service.messaging.publisher.NotificationPublisher;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.service.ArticleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ExpiryDateNotifier {

    private final ArticleService articleService;
    private final NotificationPublisher notificationPublisher;

    public ExpiryDateNotifier(ArticleService articleService, NotificationPublisher notificationPublisher) {
        this.articleService = articleService;
        this.notificationPublisher = notificationPublisher;
    }

    //@Scheduled(cron = "0 * * * * ?") every minute
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkForExpiringItems() {
        List<Article> expiringArticles = articleService.findArticlesExpiringWithinDays(7);

        for (Article article : expiringArticles) {
            notificationPublisher.sendExpiryDateNotification(
                    article.getName(),
                    article.getExpiryDate().toString(),
                    article.getId(),
                    article.getCategory()
            );
        }
    }
}
