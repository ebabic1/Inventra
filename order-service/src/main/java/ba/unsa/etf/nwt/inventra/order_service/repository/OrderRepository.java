package ba.unsa.etf.nwt.inventra.order_service.repository;

import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(value = "Order.detail")
    @NonNull
    Order getById(@NonNull Long id);

    @EntityGraph(value = "Order.detail", type = EntityGraph.EntityGraphType.FETCH)
    @NonNull
    List<Order> findAll();
}
