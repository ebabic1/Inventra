package ba.unsa.etf.nwt.inventra.reporting_service.repository;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.dto.OrderSummaryDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Order;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT new ba.unsa.etf.nwt.inventra.reporting_service.dto.ArticleDTO(" +
            "a.article.name, " +
            "a.article.price, " +
            "a.article.category, " +
            "SUM(a.quantity)) " +
            "FROM OrderArticle a " +
            "GROUP BY a.article.name, a.article.price, a.article.category " +
            "ORDER BY SUM(a.quantity) DESC")
    List<ArticleDTO> findArticlesOrderedByQuantity();

    @Query("SELECT new ba.unsa.etf.nwt.inventra.reporting_service.dto.OrderSummaryDTO(" +
            "o.id, " +
            "o.orderDate, " +
            "a.name, " +
            "oa.quantity, " +
            "CAST(a.price * oa.quantity AS double)) " +
            "FROM Order o " +
            "JOIN o.orderArticles oa " +
            "JOIN oa.article a " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "ORDER BY o.id, o.orderDate")
    List<OrderSummaryDTO> getOrderSummaries(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
