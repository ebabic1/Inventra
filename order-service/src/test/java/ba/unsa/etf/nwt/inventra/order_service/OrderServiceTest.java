package ba.unsa.etf.nwt.inventra.order_service;

import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.model.Supplier;
import ba.unsa.etf.nwt.inventra.order_service.repository.OrderRepository;
import ba.unsa.etf.nwt.inventra.order_service.repository.SupplierRepository;
import ba.unsa.etf.nwt.inventra.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);

        order = new Order();
        order.setId(1L);
        order.setSupplier(supplier);
    }

    @Test
    void testFindById_ShouldReturnOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Optional<Order> result = orderService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testCreate_ShouldSaveAndReturnOrder() {
        when(supplierRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.create(order);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testCreate_ShouldThrowException_WhenSupplierNotFound() {
        when(supplierRepository.existsById(1L)).thenReturn(false);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> orderService.create(order));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testUpdate_ShouldReturnUpdatedOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(supplierRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.save(order)).thenReturn(order);

        Order updatedOrder = orderService.update(1L, order);
        assertNotNull(updatedOrder);
        assertEquals(1L, updatedOrder.getId());
    }

    @Test
    void testUpdate_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> orderService.update(1L, order));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testDelete_ShouldDeleteOrder() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);
        assertDoesNotThrow(() -> orderService.delete(1L));
    }

    @Test
    void testDelete_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.existsById(1L)).thenReturn(false);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> orderService.delete(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}