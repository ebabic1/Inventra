package ba.unsa.etf.nwt.inventra.order_service;

import ba.unsa.etf.nwt.inventra.order_service.controller.OrderArticleController;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderArticleDTO;
import ba.unsa.etf.nwt.inventra.order_service.mapper.OrderArticleMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.OrderArticle;
import ba.unsa.etf.nwt.inventra.order_service.service.OrderArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderArticleController.class)
@ExtendWith(MockitoExtension.class)
public class OrderArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderArticleService orderArticleService;

    @MockitoBean
    private OrderArticleMapper orderArticleMapper;

    @Test
    void getAllOrderArticles_ShouldReturnList() throws Exception {
        List<OrderArticle> orderArticles = getOrderArticles();

        when(orderArticleService.findAll()).thenReturn(orderArticles);
        when(orderArticleMapper.toDTO(any())).thenAnswer(invocation -> {
            OrderArticle oa = invocation.getArgument(0);
            OrderArticleDTO dto = new OrderArticleDTO();
            dto.setQuantity(oa.getQuantity());
            return dto;
        });

        mockMvc.perform(get("/api/order-articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    private static List<OrderArticle> getOrderArticles() {
        OrderArticle orderArticle1 = new OrderArticle();
        orderArticle1.setId(1L);
        orderArticle1.setQuantity(5);

        OrderArticle orderArticle2 = new OrderArticle();
        orderArticle2.setId(2L);
        orderArticle2.setQuantity(10);

        return List.of(orderArticle1, orderArticle2);
    }

    @Test
    void getOrderArticleById_ShouldReturnOrderArticle() throws Exception {
        OrderArticle orderArticle = new OrderArticle();
        orderArticle.setId(1L);
        orderArticle.setQuantity(5);

        OrderArticleDTO orderArticleDTO = new OrderArticleDTO();
        orderArticleDTO.setQuantity(5);

        when(orderArticleService.findById(1L)).thenReturn(Optional.of(orderArticle));
        when(orderArticleMapper.toDTO(orderArticle)).thenReturn(orderArticleDTO);

        mockMvc.perform(get("/api/order-articles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void getOrderArticleById_ShouldReturnNotFound() throws Exception {
        when(orderArticleService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/order-articles/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOrderArticle_ShouldReturnNoContent() throws Exception {
        doNothing().when(orderArticleService).delete(1L);

        mockMvc.perform(delete("/api/order-articles/1"))
                .andExpect(status().isNoContent());
    }
}
