package ba.unsa.etf.nwt.inventra.inventory_service.controller;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.LocationDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.service.LocationService;
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

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        List<LocationDTO> locations = locationService.findAll();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        return locationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO) {
        return locationService.create(locationDTO)
                .map(location -> ResponseEntity.status(HttpStatus.CREATED).body(location))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable Long id, @RequestBody LocationDTO locationDTO) {
        return locationService.update(id, locationDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
