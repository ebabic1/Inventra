package ba.unsa.etf.nwt.inventra.inventory_service;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.WarehouseRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.service.WarehouseService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class WarehouseServiceTest {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Test
    void createWarehouse_ShouldSaveToDatabase() {
        Warehouse warehouse = new Warehouse();
        warehouse.setName("New Warehouse");
        warehouse.setAddress("New Street 123");
        warehouse.setZipCode("71000");

        Warehouse savedWarehouse = warehouseService.create(warehouse);

        assertNotNull(savedWarehouse.getId());
        assertEquals("New Warehouse", savedWarehouse.getName());
    }

    @Test
    void findById_ShouldReturnWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setName("Test Warehouse");
        warehouse.setAddress("Address 456");
        warehouse.setZipCode("72000");

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);

        Optional<Warehouse> foundWarehouse = warehouseService.findById(savedWarehouse.getId());

        assertTrue(foundWarehouse.isPresent());
        assertEquals("Test Warehouse", foundWarehouse.get().getName());
    }
}
