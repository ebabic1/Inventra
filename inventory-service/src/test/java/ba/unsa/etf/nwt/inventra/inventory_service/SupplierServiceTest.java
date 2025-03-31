package ba.unsa.etf.nwt.inventra.inventory_service;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.SupplierDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.mapper.SupplierMapper;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Supplier;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.SupplierRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.service.SupplierService;
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
class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierMapper supplierMapper;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier supplier;
    private SupplierDTO supplierDTO;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Test Supplier");

        supplierDTO = new SupplierDTO();
        supplierDTO.setId(1L);
        supplierDTO.setName("Test Supplier");
    }

    @Test
    void testFindById_ShouldReturnSupplierDTO() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierMapper.toDTO(supplier)).thenReturn(supplierDTO);

        Optional<SupplierDTO> result = supplierService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Supplier", result.get().getName());
    }

    @Test
    void testCreate_ShouldSaveAndReturnSupplierDTO() {
        when(supplierMapper.toEntity(supplierDTO)).thenReturn(supplier);
        when(supplierRepository.save(supplier)).thenReturn(supplier);
        when(supplierMapper.toDTO(supplier)).thenReturn(supplierDTO);

        SupplierDTO result = supplierService.create(supplierDTO);

        assertEquals("Test Supplier", result.getName());
    }

    @Test
    void testDelete_ShouldRemoveSupplier() {
        when(supplierRepository.existsById(1L)).thenReturn(true);

        supplierService.delete(1L);

        verify(supplierRepository, times(1)).deleteById(1L);
    }
}
