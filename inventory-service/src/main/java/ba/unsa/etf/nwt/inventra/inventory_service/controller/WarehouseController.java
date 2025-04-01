package ba.unsa.etf.nwt.inventra.inventory_service.controller;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.WarehouseDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.mapper.WarehouseMapper;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import ba.unsa.etf.nwt.inventra.inventory_service.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final WarehouseMapper warehouseMapper;

    @GetMapping
    public ResponseEntity<List<WarehouseDTO>> getAllWarehouses() {
        List<WarehouseDTO> warehouses = warehouseService.findAll().stream()
                .map(warehouseMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDTO> getWarehouseById(@PathVariable Long id) {
        return warehouseService.findById(id)
                .map(warehouseMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WarehouseDTO> createWarehouse(@Valid @RequestBody WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        Warehouse createdWarehouse = warehouseService.create(warehouse);
        return ResponseEntity
                .created(URI.create("/api/warehouses/" + createdWarehouse.getId()))
                .body(warehouseMapper.toDTO(createdWarehouse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDTO> updateWarehouse(@PathVariable Long id,
                                                        @Valid @RequestBody WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        Warehouse updatedWarehouse = warehouseService.update(id, warehouse);
        return ResponseEntity.ok(warehouseMapper.toDTO(updatedWarehouse));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWarehouse(@PathVariable Long id) {
        warehouseService.delete(id);
    }
}
