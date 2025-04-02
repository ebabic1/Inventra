package ba.unsa.etf.nwt.inventra.order_service;

import ba.unsa.etf.nwt.inventra.order_service.controller.SupplierController;
import ba.unsa.etf.nwt.inventra.order_service.dto.SupplierDTO;
import ba.unsa.etf.nwt.inventra.order_service.service.SupplierService;
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

@WebMvcTest(SupplierController.class)
@ExtendWith(MockitoExtension.class)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SupplierService supplierService;

    @Test
    void testGetAllSuppliers_ShouldReturnEmptyList() throws Exception {
        when(supplierService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetSupplierById_ShouldReturnSupplier() throws Exception {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setName("Test Supplier");

        when(supplierService.findById(1L)).thenReturn(Optional.of(supplierDTO));

        mockMvc.perform(get("/api/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Supplier")));
    }

    @Test
    void testGetSupplierById_NotFound_ShouldReturnNotFound() throws Exception {
        when(supplierService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/suppliers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteSupplier_ShouldReturnNoContent() throws Exception {
        doNothing().when(supplierService).delete(1L);

        mockMvc.perform(delete("/api/suppliers/1"))
                .andExpect(status().isNoContent());

        verify(supplierService, times(1)).delete(1L);
    }
}