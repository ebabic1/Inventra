package ba.unsa.etf.nwt.inventra.order_service.repository;

import ba.unsa.etf.nwt.inventra.order_service.model.OrderArticle;
import org.springframework.lang.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderArticleRepository extends JpaRepository<OrderArticle, Long> {
    @EntityGraph(value = "OrderArticle.detail", type = EntityGraph.EntityGraphType.FETCH)
    @NonNull
    List<OrderArticle> findAll();
}
