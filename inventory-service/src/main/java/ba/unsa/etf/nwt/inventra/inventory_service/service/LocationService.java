package ba.unsa.etf.nwt.inventra.inventory_service.service;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.LocationDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.mapper.LocationMapper;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Location;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.LocationRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final WarehouseRepository warehouseRepository;
    private final LocationMapper locationMapper;

    @Transactional
    public List<LocationDTO> findAll() {
        return locationRepository.findAll().stream()
                .map(locationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<LocationDTO> findById(Long id) {
        return locationRepository.findById(id).map(locationMapper::toDTO);
    }

    @Transactional
    public Optional<LocationDTO> create(LocationDTO locationDTO) {
        Warehouse warehouse = warehouseRepository.findById(locationDTO.getWarehouseId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Warehouse not found"));

        Location location = locationMapper.toEntity(locationDTO);
        location.setWarehouse(warehouse);
        Location savedLocation = locationRepository.save(location);
        return Optional.of(locationMapper.toDTO(savedLocation));
    }

    @Transactional
    public Optional<LocationDTO> update(Long id, LocationDTO locationDTO) {
        Optional<Location> existingLocationOpt = locationRepository.findById(id);

        if (!existingLocationOpt.isPresent()) {
            return Optional.empty();
        }

        Location existingLocation = existingLocationOpt.get();

        if (!existingLocation.getWarehouse().getId().equals(locationDTO.getWarehouseId())) {
            Warehouse newWarehouse = warehouseRepository.findById(locationDTO.getWarehouseId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "New warehouse not found"));
            existingLocation.setWarehouse(newWarehouse);
        }

        Location updatedLocation = locationRepository.save(existingLocation);
        return Optional.of(locationMapper.toDTO(updatedLocation));
    }

    @Transactional
    public void delete(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found with id: " + id);
        }
        locationRepository.deleteById(id);
    }
}