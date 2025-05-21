package ba.unsa.etf.nwt.inventra.inventory_service.service;

import ba.unsa.etf.nwt.inventra.inventory_service.client.SupplierClient;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.ArticleRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.LocationRepository;
import ba.unsa.etf.nwt.system_events_service.ActionType;
import ba.unsa.etf.nwt.system_events_service.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final LocationRepository locationRepository;
    private final SystemEventsClient systemEventsClient;
    private final SupplierClient supplierClient;

    public Page<Article> findAll(Pageable pageable) {
        Page<Article> page = articleRepository.findAll(pageable);
        logEvent(ActionType.GET_ALL, "Article", ResponseType.SUCCESS);
        return page;
    }

    public Optional<Article> findById(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        logEvent(ActionType.GET, "Article", article.isPresent() ? ResponseType.SUCCESS : ResponseType.FAILURE);
        return article;
    }

    @Transactional
    public List<Article> createBatch(List<Article> articles) {
        articles.forEach(this::validateArticleDependencies);
        List<Article> savedArticles = articleRepository.saveAll(articles);
        logEvent(ActionType.CREATE_BATCH, "Article", ResponseType.SUCCESS);
        return savedArticles;
    }

    @Transactional
    public Article create(Article article) {
        validateArticleDependencies(article);
        Article saved = articleRepository.save(article);
        logEvent(ActionType.CREATE, "Article", ResponseType.SUCCESS);
        return saved;
    }

    @Transactional
    public Article update(Long id, Article article) {
        articleRepository.findById(id)
                .orElseThrow(() -> failUpdate("Article not found with id: " + id));

        validateArticleDependencies(article);
        article.setId(id);
        Article updated = articleRepository.save(article);
        logEvent(ActionType.UPDATE, "Article", ResponseType.SUCCESS);
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        if (!articleRepository.existsById(id)) {
            throw failDelete("Article not found with id: " + id);
        }
        articleRepository.deleteById(id);
        logEvent(ActionType.DELETE, "Article", ResponseType.SUCCESS);
    }

    @Transactional
    public Article patchArticle(Long id, Article updates) {
        Article existing = articleRepository.findById(id)
                .orElseThrow(() -> failPatch("Article not found with id: " + id));

        if (updates.getName() != null) existing.setName(updates.getName());
        if (updates.getPrice() != null) existing.setPrice(updates.getPrice());
        if (updates.getQuantity() != null) existing.setQuantity(updates.getQuantity());

        if (updates.getLocation() != null && updates.getLocation().getId() != null) {
            Long locationId = updates.getLocation().getId();
            if (!locationRepository.existsById(locationId)) {
                throw failPatch("Location not found with id: " + locationId);
            }
            existing.setLocation(updates.getLocation());
        }

        Article patched = articleRepository.save(existing);
        logEvent(ActionType.PATCH, "Article", ResponseType.SUCCESS);
        return patched;
    }

    private void validateArticleDependencies(Article article) {
        if (!supplierClient.checkSupplierExists(article.getSupplierId())) {
            throw fail(ActionType.CREATE, "Supplier not found with id: " + article.getSupplierId());
        }
        if (!locationRepository.existsById(article.getLocation().getId())) {
            throw fail(ActionType.CREATE, "Location not found with id: " + article.getLocation().getId());
        }
    }

    private ResponseStatusException fail(ActionType actionType, String msg) {
        logEvent(actionType, "Article", ResponseType.FAILURE);
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }

    private ResponseStatusException failUpdate(String msg) {
        logEvent(ActionType.UPDATE, "Article", ResponseType.FAILURE);
        return new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
    }

    private ResponseStatusException failDelete(String msg) {
        logEvent(ActionType.DELETE, "Article", ResponseType.FAILURE);
        return new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
    }

    private ResponseStatusException failPatch(String msg) {
        logEvent(ActionType.PATCH, "Article", ResponseType.FAILURE);
        return new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
    }

    private void logEvent(ActionType actionType, String resourceName, ResponseType responseType) {
        try {
            systemEventsClient.logEvent(
                    Instant.now().toString(),
                    "inventory-service",
                    "current-user",
                    actionType,
                    resourceName,
                    responseType
            );
        } catch (Exception e) {
            log.error("Failed to log system event: {}", e.toString());
        }
    }
}
