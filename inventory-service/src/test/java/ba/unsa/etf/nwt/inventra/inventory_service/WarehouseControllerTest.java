package ba.unsa.etf.nwt.inventra.inventory_service;

import ba.unsa.etf.nwt.inventra.inventory_service.controller.WarehouseController;
import ba.unsa.etf.nwt.inventra.inventory_service.mapper.WarehouseMapperImpl;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.WarehouseRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.service.WarehouseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WarehouseController.class)
@Import({
        WarehouseService.class,
        WarehouseMapperImpl.class,
        ValidationAutoConfiguration.class,
})
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseRepository warehouseRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getWarehouseById_WhenExists_ShouldReturnWarehouse() throws Exception {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("Main Warehouse");

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));

        mockMvc.perform(get("/api/warehouses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Main Warehouse"));
    }

    @Test
    void getWarehouseById_WhenNotExists_ShouldReturn404() throws Exception {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/warehouses/1"))
                .andExpect(status().isNotFound());
    }


}
