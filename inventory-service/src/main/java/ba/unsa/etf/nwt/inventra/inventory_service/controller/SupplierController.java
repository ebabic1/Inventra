package ba.unsa.etf.nwt.inventra.inventory_service.controller;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.SupplierDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @Operation(summary = "Get all suppliers", description = "Retrieve a list of all suppliers in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of suppliers")
    })
    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        List<SupplierDTO> suppliers = supplierService.findAll();
        return ResponseEntity.ok(suppliers);
    }

    @Operation(summary = "Get supplier by ID", description = "Retrieve a specific supplier by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the supplier"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(
            @Parameter(description = "ID of the supplier to be retrieved") @PathVariable Long id) {
        return supplierService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new supplier", description = "Create a new supplier and store it in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the supplier"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(
            @Parameter(description = "Supplier data to be created") @RequestBody SupplierDTO supplierDTO) {
        SupplierDTO createdSupplier = supplierService.create(supplierDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupplier);
    }

    @Operation(summary = "Update an existing supplier", description = "Update the details of an existing supplier by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the supplier"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(
            @Parameter(description = "ID of the supplier to be updated") @PathVariable Long id,
            @Parameter(description = "Updated supplier data") @RequestBody SupplierDTO supplierDTO) {
        SupplierDTO updatedSupplier = supplierService.update(id, supplierDTO);
        return ResponseEntity.ok(updatedSupplier);
    }

    @Operation(summary = "Delete a supplier", description = "Delete a supplier from the system by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the supplier"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(
            @Parameter(description = "ID of the supplier to be deleted") @PathVariable Long id) {
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
