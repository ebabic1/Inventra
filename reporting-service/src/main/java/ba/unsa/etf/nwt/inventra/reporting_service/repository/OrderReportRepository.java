package ba.unsa.etf.nwt.inventra.reporting_service.repository;

import ba.unsa.etf.nwt.inventra.reporting_service.model.OrderReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderReportRepository extends JpaRepository<OrderReport, Long> {
}
