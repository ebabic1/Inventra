package ba.unsa.etf.nwt.inventra.reporting_service.repository;

import ba.unsa.etf.nwt.inventra.reporting_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT MAX(o.id) FROM Order o")
    Optional<Long> findMaxId();
}
