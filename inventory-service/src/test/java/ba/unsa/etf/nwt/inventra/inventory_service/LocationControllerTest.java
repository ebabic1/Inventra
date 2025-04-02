package ba.unsa.etf.nwt.inventra.inventory_service;

import ba.unsa.etf.nwt.inventra.inventory_service.controller.LocationController;
import ba.unsa.etf.nwt.inventra.inventory_service.dto.LocationDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.service.LocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocationService locationService;

    @Test
    void testGetAllLocations_ShouldReturnEmptyList() throws Exception {
        when(locationService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetLocationById_ShouldReturnLocation() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setWarehouseId(1L);

        when(locationService.findById(1L)).thenReturn(Optional.of(locationDTO));

        mockMvc.perform(get("/api/locations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.warehouseId", is(1)));
    }

    @Test
    void testDeleteLocation_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/locations/1"))
                .andExpect(status().isNoContent());

        verify(locationService, times(1)).delete(1L);
    }
}
