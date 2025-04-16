package ba.unsa.etf.nwt.inventra.inventory_service;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Location;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.ArticleRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.LocationRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.service.ArticleService;
import ba.unsa.etf.nwt.inventra.inventory_service.client.SupplierClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private SupplierClient supplierClient;

    @InjectMocks
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        PageRequest pageable = PageRequest.of(0, 10);
        Article article = new Article();
        article.setName("Test Article");
        Page<Article> articlePage = new PageImpl<>(List.of(article));

        when(articleRepository.findAll(pageable)).thenReturn(articlePage);

        Page<Article> result = articleService.findAll(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Test Article", result.getContent().getFirst().getName());
        verify(articleRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindById_Found() {
        Article article = new Article();
        article.setId(1L);
        article.setName("Test Article");

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        Optional<Article> result = articleService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Article", result.get().getName());
        verify(articleRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Article> result = articleService.findById(1L);

        assertFalse(result.isPresent());
        verify(articleRepository, times(1)).findById(1L);
    }

    @Test
    void testCreate_Success() {
        Article article = new Article();
        article.setName("New Article");
        article.setSupplierId(123L);
        Location location = new Location();
        location.setId(1L);
        article.setLocation(location);

        when(supplierClient.checkSupplierExists(123L)).thenReturn(true);
        when(locationRepository.existsById(1L)).thenReturn(true);
        when(articleRepository.save(article)).thenReturn(article);

        Article result = articleService.create(article);

        assertNotNull(result);
        assertEquals("New Article", result.getName());
        verify(articleRepository, times(1)).save(article);
    }

    @Test
    void testCreate_InvalidSupplier() {
        Article article = new Article();
        article.setSupplierId(123L);
        Location location = new Location();
        location.setId(1L);
        article.setLocation(location);

        when(supplierClient.checkSupplierExists(123L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            articleService.create(article);
        });

        assertEquals("400 BAD_REQUEST \"Supplier not found with ID: 123\"", exception.getMessage());
        verify(supplierClient, times(1)).checkSupplierExists(123L);
        verify(articleRepository, never()).save(article);
    }

    @Test
    void testUpdate_NotFound() {
        Article article = new Article();
        article.setId(1L);

        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            articleService.update(1L, article);
        });

        assertEquals("404 NOT_FOUND \"Article not found with id: 1\"", exception.getMessage());
        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, never()).save(article);
    }

    @Test
    void testDelete_Success() {
        when(articleRepository.existsById(1L)).thenReturn(true);

        articleService.delete(1L);

        verify(articleRepository, times(1)).existsById(1L);
        verify(articleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(articleRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            articleService.delete(1L);
        });

        assertEquals("404 NOT_FOUND \"Article not found with id: 1\"", exception.getMessage());
        verify(articleRepository, times(1)).existsById(1L);
        verify(articleRepository, never()).deleteById(1L);
    }
}
