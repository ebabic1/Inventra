package ba.unsa.etf.nwt.inventra.order_service;

import ba.unsa.etf.nwt.inventra.order_service.client.InventoryClient;
import ba.unsa.etf.nwt.inventra.order_service.dto.ArticleResponseDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.LocationResponseDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDetailsRequestDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDetailsResponseDTO;
import ba.unsa.etf.nwt.inventra.order_service.mapper.OrderMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.model.OrderStatus;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private Supplier supplier;
    private OrderDetailsRequestDTO orderDetailsRequestDTO;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setId(1L);

        order = new Order();
        order.setId(1L);
        order.setSupplier(supplier);

        orderDetailsRequestDTO = new OrderDetailsRequestDTO();
        orderDetailsRequestDTO.setSupplierId(1L);
        orderDetailsRequestDTO.setArticles(Collections.emptyList());
    }

    @Test
    void testFindById_ShouldReturnOrderDetailsResponseDTO() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        OrderDetailsResponseDTO responseDTO = new OrderDetailsResponseDTO();
        when(orderMapper.toDetailsResponseDTO(order)).thenReturn(responseDTO);

        OrderDetailsResponseDTO result = orderService.findById(1L);
        assertNotNull(result);
    }

    @Test
    void testCreate_ShouldSaveAndReturnOrder() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(orderMapper.fromOrderDetails(orderDetailsRequestDTO, supplier)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.create(orderDetailsRequestDTO);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testCreate_ShouldThrowException_WhenSupplierNotFound() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderService.create(orderDetailsRequestDTO));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testUpdate_ShouldReturnUpdatedOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(orderMapper.fromOrderDetails(orderDetailsRequestDTO, supplier)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.update(1L, orderDetailsRequestDTO);
        assertNotNull(updatedOrder);
        assertEquals(1L, updatedOrder.getId());
    }

    @Test
    void testUpdate_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderService.update(1L, orderDetailsRequestDTO));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testDelete_ShouldDeleteOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).deleteById(1L);

        assertDoesNotThrow(() -> orderService.delete(1L));
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderService.delete(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testPatch_ShouldUpdateOrderStatus() {
        Order orderUpdates = new Order();
        orderUpdates.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order patchedOrder = orderService.patch(1L, orderUpdates);

        assertNotNull(patchedOrder);
        assertEquals(OrderStatus.PENDING, patchedOrder.getStatus());
    }

    @Test
    void testPatch_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderService.patch(1L, new Order()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
