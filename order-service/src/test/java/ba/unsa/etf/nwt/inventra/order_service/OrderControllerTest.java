package ba.unsa.etf.nwt.inventra.order_service;

import ba.unsa.etf.nwt.inventra.order_service.controller.OrderController;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDetailsRequestDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDetailsResponseDTO;
import ba.unsa.etf.nwt.inventra.order_service.mapper.OrderMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

        when(orderMapper.toDTO(any(Order.class))).thenAnswer(invocation -> {
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
    void getOrderById_ShouldReturnOrderDetailsResponseDTO() throws Exception {
        OrderDetailsResponseDTO responseDTO = new OrderDetailsResponseDTO();
        responseDTO.setName("Test Order");

        when(orderService.findById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Order"));
    }

    @Test
    void deleteOrder_ShouldReturnNoContent() throws Exception {
        doNothing().when(orderService).delete(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void patchOrder_ShouldReturnUpdatedOrder() throws Exception {
        OrderDTO updates = new OrderDTO();
        updates.setName("Updated Name");

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setName("Updated Name");

        when(orderMapper.toEntity(any(OrderDTO.class))).thenReturn(updatedOrder);
        when(orderService.patch(eq(1L), any(Order.class))).thenReturn(updatedOrder);
        when(orderMapper.toDTO(updatedOrder)).thenReturn(updates);

        mockMvc.perform(patch("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() throws Exception {
        OrderDetailsRequestDTO requestDTO = new OrderDetailsRequestDTO();
        requestDTO.setName("New Order");

        Order newOrder = new Order();
        newOrder.setId(1L);
        newOrder.setName("New Order");

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setName("New Order");

        when(orderService.create(any(OrderDetailsRequestDTO.class))).thenReturn(newOrder);
        when(orderMapper.toDTO(newOrder)).thenReturn(orderDTO);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Order"));
    }
}
