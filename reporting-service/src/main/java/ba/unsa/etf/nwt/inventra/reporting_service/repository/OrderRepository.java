package ba.unsa.etf.nwt.inventra.reporting_service.repository;

import ba.unsa.etf.nwt.inventra.reporting_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
