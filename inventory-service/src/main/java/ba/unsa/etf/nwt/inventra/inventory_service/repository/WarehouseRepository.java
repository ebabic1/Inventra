package ba.unsa.etf.nwt.inventra.inventory_service.repository;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}