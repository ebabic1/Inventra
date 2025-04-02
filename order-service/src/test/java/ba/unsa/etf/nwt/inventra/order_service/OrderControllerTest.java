package ba.unsa.etf.nwt.inventra.order_service;

import ba.unsa.etf.nwt.inventra.order_service.controller.OrderController;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDTO;
import ba.unsa.etf.nwt.inventra.order_service.mapper.OrderMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private OrderMapper orderMapper;

    @Test
    void getAllOrders_ShouldReturnList() throws Exception {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setName("Order 1");
        order1.setOrderDate(LocalDate.now());
        order1.setDeliveryDate(LocalDate.now().plusDays(5));

        Order order2 = new Order();
        order2.setId(2L);
        order2.setName("Order 2");
        order2.setOrderDate(LocalDate.now());
        order2.setDeliveryDate(LocalDate.now().plusDays(3));

        when(orderService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(order1, order2)));
        when(orderMapper.toDTO(any())).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setName(o.getName());
            return orderDTO;
        });

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getOrderById_ShouldReturnOrder() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setName("Test Order");
        order.setOrderDate(LocalDate.now());
        order.setDeliveryDate(LocalDate.now().plusDays(3));

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setName("Test Order");

        when(orderService.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Order"));
    }

    @Test
    void getOrderById_ShouldReturnNotFound() throws Exception {
        when(orderService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOrder_ShouldReturnNoContent() throws Exception {
        doNothing().when(orderService).delete(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());
    }
}
