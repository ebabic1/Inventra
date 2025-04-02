package ba.unsa.etf.nwt.inventra.inventory_service.service;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.ArticleRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.LocationRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final SupplierRepository supplierRepository;
    private final LocationRepository locationRepository;

    public Page<Article> findAll(Pageable pageable) {
        Page<Article> page = articleRepository.findAll(pageable);
        return page != null ? page : Page.empty();
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    @Transactional
    public List<Article> createBatch(List<Article> articles) {
        for (Article article : articles) {
            if (!supplierRepository.existsById(article.getSupplier().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier not found with ID: " + article.getSupplier().getId());
            }
            if (!locationRepository.existsById(article.getLocation().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location not found with ID: " + article.getLocation().getId());
            }
        }
        return articleRepository.saveAll(articles);
    }

    @Transactional
    public Article create(Article article) {
        if (!supplierRepository.existsById(article.getSupplier().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier not found with ID: " + article.getSupplier().getId());
        }
        if (!locationRepository.existsById(article.getLocation().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location not found with ID: " + article.getLocation().getId());
        }
        return articleRepository.save(article);
    }

    @Transactional
    public Article update(Long id, Article article) {
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found with id: " + id));
        if (!locationRepository.existsById(article.getLocation().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location not found with id: " + article.getLocation().getId());
        }
        if (!supplierRepository.existsById(article.getSupplier().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier not found with id: " + article.getSupplier().getId());
        }
        article.setId(existingArticle.getId());
        return articleRepository.save(article);
    }

    @Transactional
    public void delete(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found with id: " + id);
        }
        articleRepository.deleteById(id);
    }

    @Transactional
    public Article patchArticle(Long id, Article articleUpdates) {
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found with id: " + id));

        // TODO: Change if needed
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
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found with id: " + supplierId);
            }
            existingArticle.setSupplier(articleUpdates.getSupplier());
        }
        if (articleUpdates.getLocation() != null && articleUpdates.getLocation().getId() != null) {
            Long locationId = articleUpdates.getLocation().getId();
            if (!locationRepository.existsById(locationId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found with id: " + locationId);
            }
            existingArticle.setLocation(articleUpdates.getLocation());
        }
        return articleRepository.save(existingArticle);
    }
}
