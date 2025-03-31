package ba.unsa.etf.nwt.inventra.inventory_service;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.LocationDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.mapper.LocationMapper;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Location;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.LocationRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.WarehouseRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationService locationService;

    private Location location;
    private Warehouse warehouse;
    private LocationDTO locationDTO;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        warehouse.setId(1L);

        location = new Location();
        location.setId(1L);
        location.setWarehouse(warehouse);

        locationDTO = new LocationDTO();
        locationDTO.setId(1L);
        locationDTO.setWarehouseId(1L);
    }

    @Test
    void testFindById_ShouldReturnLocationDTO() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(locationMapper.toDTO(location)).thenReturn(locationDTO);

        Optional<LocationDTO> result = locationService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testCreate_ShouldSaveAndReturnLocationDTO() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(locationMapper.toEntity(locationDTO)).thenReturn(location);
        when(locationRepository.save(location)).thenReturn(location);
        when(locationMapper.toDTO(location)).thenReturn(locationDTO);

        Optional<LocationDTO> result = locationService.create(locationDTO);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }
}
