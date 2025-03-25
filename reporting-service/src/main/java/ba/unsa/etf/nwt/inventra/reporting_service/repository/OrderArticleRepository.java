package ba.unsa.etf.nwt.inventra.reporting_service.repository;

import ba.unsa.etf.nwt.inventra.reporting_service.model.OrderArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderArticleRepository extends JpaRepository<OrderArticle, Long> {
}
