package ba.unsa.etf.nwt.inventra.reporting_service.repository;

import ba.unsa.etf.nwt.inventra.reporting_service.model.OrderReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderReportRepository extends JpaRepository<OrderReport, Long> {
    void deleteByOrderArticleIdIn(List<Long> ids);
}
