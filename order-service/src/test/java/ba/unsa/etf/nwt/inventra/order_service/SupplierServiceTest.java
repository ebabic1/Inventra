package ba.unsa.etf.nwt.inventra.order_service;

import ba.unsa.etf.nwt.inventra.order_service.dto.SupplierDTO;
import ba.unsa.etf.nwt.inventra.order_service.mapper.SupplierMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.Supplier;
import ba.unsa.etf.nwt.inventra.order_service.repository.SupplierRepository;
import ba.unsa.etf.nwt.inventra.order_service.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
        supplierDTO.setName("Test Supplier");
    }

    @Test
    void testFindAll_ShouldReturnListOfSuppliers() {
        when(supplierRepository.findAll()).thenReturn(List.of(supplier));
        when(supplierMapper.toDTO(supplier)).thenReturn(supplierDTO);

        List<SupplierDTO> result = supplierService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Supplier", result.get(0).getName());
    }

    @Test
    void testFindById_ShouldReturnSupplier() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierMapper.toDTO(supplier)).thenReturn(supplierDTO);

        Optional<SupplierDTO> result = supplierService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Supplier", result.get().getName());
    }

    @Test
    void testFindById_NotFound_ShouldReturnEmptyOptional() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<SupplierDTO> result = supplierService.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testCreate_ShouldSaveAndReturnSupplier() {
        when(supplierMapper.toEntity(supplierDTO)).thenReturn(supplier);
        when(supplierRepository.save(supplier)).thenReturn(supplier);
        when(supplierMapper.toDTO(supplier)).thenReturn(supplierDTO);

        SupplierDTO result = supplierService.create(supplierDTO);

        assertNotNull(result);
        assertEquals("Test Supplier", result.getName());
    }

    @Test
    void testUpdate_NotFound_ShouldThrowException() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> supplierService.update(1L, supplierDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testDelete_ShouldDeleteSupplier() {
        when(supplierRepository.existsById(1L)).thenReturn(true);
        doNothing().when(supplierRepository).deleteById(1L);

        assertDoesNotThrow(() -> supplierService.delete(1L));
        verify(supplierRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound_ShouldThrowException() {
        when(supplierRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> supplierService.delete(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}