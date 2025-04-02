package ba.unsa.etf.nwt.inventra.order_service;

import ba.unsa.etf.nwt.inventra.order_service.model.Article;
import ba.unsa.etf.nwt.inventra.order_service.repository.ArticleRepository;
import ba.unsa.etf.nwt.inventra.order_service.service.ArticleService;
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
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article();
        article.setId(1L);
        article.setName("Test Article");
    }

    @Test
    void testFindById_ShouldReturnArticle() {
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        Optional<Article> result = articleService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Article", result.get().getName());
    }

    @Test
    void testFindById_NotFound_ShouldReturnEmptyOptional() {
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Article> result = articleService.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testCreate_ShouldSaveAndReturnArticle() {
        when(articleRepository.save(article)).thenReturn(article);

        Article result = articleService.create(article);

        assertNotNull(result);
        assertEquals("Test Article", result.getName());
    }

    @Test
    void testUpdate_NotFound_ShouldThrowException() {
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        Article updatedArticle = new Article();
        updatedArticle.setName("Updated Article");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> articleService.update(1L, updatedArticle));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testDelete_ShouldDeleteArticle() {
        when(articleRepository.existsById(1L)).thenReturn(true);
        doNothing().when(articleRepository).deleteById(1L);

        assertDoesNotThrow(() -> articleService.delete(1L));
        verify(articleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound_ShouldThrowException() {
        when(articleRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> articleService.delete(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
