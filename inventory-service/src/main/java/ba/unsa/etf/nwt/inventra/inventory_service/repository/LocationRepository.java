package ba.unsa.etf.nwt.inventra.inventory_service.repository;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
