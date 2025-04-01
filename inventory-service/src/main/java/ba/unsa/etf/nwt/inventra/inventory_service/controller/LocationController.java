package ba.unsa.etf.nwt.inventra.inventory_service.controller;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.LocationDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @Operation(summary = "Get all locations", description = "Retrieve a list of all locations in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of locations")
    })
    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        List<LocationDTO> locations = locationService.findAll();
        return ResponseEntity.ok(locations);
    }

    @Operation(summary = "Get location by ID", description = "Retrieve a specific location by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the location"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(
            @Parameter(description = "ID of the location to be retrieved") @PathVariable Long id) {
        return locationService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new location", description = "Create a new location and store it in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the location"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(
            @Valid @RequestBody LocationDTO locationDTO) {
        LocationDTO createdLocation = locationService.create(locationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
    }

    @Operation(summary = "Update an existing location", description = "Update the details of an existing location by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the location"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(
            @Parameter(description = "ID of the location to be updated") @PathVariable Long id,
            @Valid @RequestBody LocationDTO locationDTO) {
        LocationDTO updatedLocation = locationService.update(id, locationDTO);
        return ResponseEntity.ok(updatedLocation);
    }

    @Operation(summary = "Delete a location", description = "Delete a location from the system by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the location"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @Parameter(description = "ID of the location to be deleted") @PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
