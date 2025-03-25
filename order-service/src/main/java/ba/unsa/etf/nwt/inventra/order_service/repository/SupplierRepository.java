package ba.unsa.etf.nwt.inventra.order_service.repository;

import ba.unsa.etf.nwt.inventra.order_service.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier,Long> {
}
