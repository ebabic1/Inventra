package ba.unsa.etf.nwt.inventra.inventory_service.controller;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.WarehouseDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.mapper.WarehouseMapper;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import ba.unsa.etf.nwt.inventra.inventory_service.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all warehouses", description = "Retrieve a list of all warehouses in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of warehouses")
    })
    @GetMapping
    public ResponseEntity<List<WarehouseDTO>> getAllWarehouses() {
        List<WarehouseDTO> warehouses = warehouseService.findAll().stream()
                .map(warehouseMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(warehouses);
    }

    @Operation(summary = "Get warehouse by ID", description = "Retrieve a specific warehouse by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the warehouse"),
            @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDTO> getWarehouseById(
            @Parameter(description = "ID of the warehouse to be retrieved") @PathVariable Long id) {
        return warehouseService.findById(id)
                .map(warehouseMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new warehouse", description = "Create a new warehouse and store it in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the warehouse"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<WarehouseDTO> createWarehouse(
            @Parameter(description = "Warehouse data to be created") @Valid @RequestBody WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        Warehouse createdWarehouse = warehouseService.create(warehouse);
        return ResponseEntity
                .created(URI.create("/api/warehouses/" + createdWarehouse.getId()))
                .body(warehouseMapper.toDTO(createdWarehouse));
    }

    @Operation(summary = "Update an existing warehouse", description = "Update the details of an existing warehouse by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the warehouse"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDTO> updateWarehouse(
            @Parameter(description = "ID of the warehouse to be updated") @PathVariable Long id,
            @Parameter(description = "Updated warehouse data") @Valid @RequestBody WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        Warehouse updatedWarehouse = warehouseService.update(id, warehouse);
        return ResponseEntity.ok(warehouseMapper.toDTO(updatedWarehouse));
    }

    @Operation(summary = "Delete a warehouse", description = "Delete a warehouse from the system by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the warehouse"),
            @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWarehouse(
            @Parameter(description = "ID of the warehouse to be deleted") @PathVariable Long id) {
        warehouseService.delete(id);
    }
}
