package ba.unsa.etf.nwt.inventra.inventory_service.service;

import ba.unsa.etf.nwt.inventra.inventory_service.client.SupplierClient;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.ArticleRepository;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final LocationRepository locationRepository;

    @Autowired
    private SupplierClient supplierClient;

    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    @Transactional
    public List<Article> createBatch(List<Article> articles) {
        for (Article article : articles) {
            // ToDo: call endpoint
            if (!locationRepository.existsById(article.getLocation().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location not found with ID: " + article.getLocation().getId());
            }
        }
        return articleRepository.saveAll(articles);
    }

    @Transactional
    public Article create(Article article) {
        if (supplierClient.checkSupplierExists(article.getSupplierId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier not found with ID: " + article.getSupplierId());
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
        if (supplierClient.checkSupplierExists(article.getSupplierId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier not found with ID: " + article.getSupplierId());
        }
        if (!locationRepository.existsById(article.getLocation().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location not found with id: " + article.getLocation().getId());
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
