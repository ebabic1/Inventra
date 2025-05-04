package ba.unsa.etf.nwt.inventra.inventory_service.service;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.ArticleRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.LocationRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.SupplierRepository;
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
    private final SupplierRepository supplierRepository;
    private final LocationRepository locationRepository;
    private final SystemEventsClient systemEventsClient; // Inject the gRPC client

    public Page<Article> findAll(Pageable pageable) {
        Page<Article> page = articleRepository.findAll(pageable);
        logEvent("GET_ALL", "Article", "SUCCESS");
        return page != null ? page : Page.empty();
    }

    public Optional<Article> findById(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            logEvent("GET", "Article", "SUCCESS");
        } else {
            logEvent("GET", "Article", "NOT_FOUND");
        }
        return article;
    }

    @Transactional
    public List<Article> createBatch(List<Article> articles) {
        for (Article article : articles) {
            if (!supplierRepository.existsById(article.getSupplier().getId())) {
                logEvent("CREATE_BATCH", "Article", "FAILURE: Supplier not found");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier not found with ID: " + article.getSupplier().getId());
            }
            if (!locationRepository.existsById(article.getLocation().getId())) {
                logEvent("CREATE_BATCH", "Article", "FAILURE: Location not found");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location not found with ID: " + article.getLocation().getId());
            }
        }
        List<Article> savedArticles = articleRepository.saveAll(articles);
        logEvent("CREATE_BATCH", "Article", "SUCCESS");
        return savedArticles;
    }

    @Transactional
    public Article create(Article article) {
        if (!supplierRepository.existsById(article.getSupplier().getId())) {
            logEvent("CREATE", "Article", "FAILURE: Supplier not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier not found with ID: " + article.getSupplier().getId());
        }
        if (!locationRepository.existsById(article.getLocation().getId())) {
            logEvent("CREATE", "Article", "FAILURE: Location not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location not found with ID: " + article.getLocation().getId());
        }
        Article savedArticle = articleRepository.save(article);
        logEvent("CREATE", "Article", "SUCCESS");
        return savedArticle;
    }

    @Transactional
    public Article update(Long id, Article article) {
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> {
                    logEvent("UPDATE", "Article", "NOT_FOUND");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found with id: " + id);
                });

        if (!locationRepository.existsById(article.getLocation().getId())) {
            logEvent("UPDATE", "Article", "FAILURE: Location not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location not found with id: " + article.getLocation().getId());
        }
        if (!supplierRepository.existsById(article.getSupplier().getId())) {
            logEvent("UPDATE", "Article", "FAILURE: Supplier not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier not found with id: " + article.getSupplier().getId());
        }

        article.setId(existingArticle.getId());
        Article updatedArticle = articleRepository.save(article);
        logEvent("UPDATE", "Article", "SUCCESS");
        return updatedArticle;
    }

    @Transactional
    public void delete(Long id) {
        if (!articleRepository.existsById(id)) {
            logEvent("DELETE", "Article", "NOT_FOUND");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found with id: " + id);
        }
        articleRepository.deleteById(id);
        logEvent("DELETE", "Article", "SUCCESS");
    }

    @Transactional
    public Article patchArticle(Long id, Article articleUpdates) {
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> {
                    logEvent("PATCH", "Article", "NOT_FOUND");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found with id: " + id);
                });

        if (articleUpdates.getName() != null) {
            existingArticle.setName(articleUpdates.getName());
        }
        if (articleUpdates.getPrice() != null) {
            existingArticle.setPrice(articleUpdates.getPrice());
        }
        if (articleUpdates.getQuantity() != null) {
            existingArticle.setQuantity(articleUpdates.getQuantity());
        }
        if (articleUpdates.getSupplier() != null && articleUpdates.getSupplier().getId() != null) {
            Long supplierId = articleUpdates.getSupplier().getId();
            if (!supplierRepository.existsById(supplierId)) {
                logEvent("PATCH", "Article", "FAILURE: Supplier not found");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found with id: " + supplierId);
            }
            existingArticle.setSupplier(articleUpdates.getSupplier());
        }
        if (articleUpdates.getLocation() != null && articleUpdates.getLocation().getId() != null) {
            Long locationId = articleUpdates.getLocation().getId();
            if (!locationRepository.existsById(locationId)) {
                logEvent("PATCH", "Article", "FAILURE: Location not found");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found with id: " + locationId);
            }
            existingArticle.setLocation(articleUpdates.getLocation());
        }

        Article patchedArticle = articleRepository.save(existingArticle);
        logEvent("PATCH", "Article", "SUCCESS");
        return patchedArticle;
    }

    private void logEvent(String actionType, String resourceName, String responseType) {
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
           log.error("Failed to log system event: " + e.getMessage());
        }
    }
}