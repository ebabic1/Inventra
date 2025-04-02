package ba.unsa.etf.nwt.inventra.order_service;

import ba.unsa.etf.nwt.inventra.order_service.model.OrderArticle;
import ba.unsa.etf.nwt.inventra.order_service.repository.OrderArticleRepository;
import ba.unsa.etf.nwt.inventra.order_service.service.OrderArticleService;
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
class OrderArticleServiceTest {

    @Mock
    private OrderArticleRepository orderArticleRepository;

    @InjectMocks
    private OrderArticleService orderArticleService;

    private OrderArticle orderArticle;

    @BeforeEach
    void setUp() {
        orderArticle = new OrderArticle();
        orderArticle.setId(1L);
        orderArticle.setQuantity(5);
    }

    @Test
    void testFindAll_ShouldReturnListOfOrderArticles() {
        when(orderArticleRepository.findAll()).thenReturn(List.of(orderArticle));

        List<OrderArticle> result = orderArticleService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(5, result.getFirst().getQuantity());
    }

    @Test
    void testFindById_ShouldReturnOrderArticle() {
        when(orderArticleRepository.findById(1L)).thenReturn(Optional.of(orderArticle));

        Optional<OrderArticle> result = orderArticleService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getQuantity());
    }

    @Test
    void testFindById_NotFound_ShouldReturnEmptyOptional() {
        when(orderArticleRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<OrderArticle> result = orderArticleService.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testCreate_ShouldSaveAndReturnOrderArticle() {
        when(orderArticleRepository.save(orderArticle)).thenReturn(orderArticle);

        OrderArticle result = orderArticleService.create(orderArticle);

        assertNotNull(result);
        assertEquals(5, result.getQuantity());
    }

    @Test
    void testUpdate_NotFound_ShouldThrowException() {
        when(orderArticleRepository.findById(1L)).thenReturn(Optional.empty());

        OrderArticle updatedOrderArticle = new OrderArticle();
        updatedOrderArticle.setQuantity(10);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderArticleService.update(1L, updatedOrderArticle));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testDelete_ShouldDeleteOrderArticle() {
        when(orderArticleRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderArticleRepository).deleteById(1L);

        assertDoesNotThrow(() -> orderArticleService.delete(1L));
        verify(orderArticleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound_ShouldThrowException() {
        when(orderArticleRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderArticleService.delete(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
