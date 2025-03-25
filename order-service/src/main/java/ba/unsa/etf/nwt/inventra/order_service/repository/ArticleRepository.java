package ba.unsa.etf.nwt.inventra.order_service.repository;

import ba.unsa.etf.nwt.inventra.order_service.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
