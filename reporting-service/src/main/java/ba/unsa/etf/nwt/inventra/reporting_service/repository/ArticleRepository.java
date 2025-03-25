package ba.unsa.etf.nwt.inventra.reporting_service.repository;

import ba.unsa.etf.nwt.inventra.reporting_service.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
