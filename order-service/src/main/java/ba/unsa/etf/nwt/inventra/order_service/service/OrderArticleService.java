package ba.unsa.etf.nwt.inventra.order_service.service;

import ba.unsa.etf.nwt.inventra.order_service.model.OrderArticle;
import ba.unsa.etf.nwt.inventra.order_service.repository.OrderArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderArticleService {
    private final OrderArticleRepository orderArticleRepository;

    public List<OrderArticle> findAll() {
        return orderArticleRepository.findAll();
    }

    public Optional<OrderArticle> findById(Long id) {
        return orderArticleRepository.findById(id);
    }

    @Transactional
    public OrderArticle create(OrderArticle article) {
        return orderArticleRepository.save(article);
    }

    @Transactional
    public OrderArticle update(Long id, OrderArticle article) {
        OrderArticle existingArticle = orderArticleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found with id: " + id));

        article.setId(existingArticle.getId());
        return orderArticleRepository.save(article);
    }

    @Transactional
    public void delete(Long id) {
        if (!orderArticleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found with id: " + id);
        }

        orderArticleRepository.deleteById(id);
    }
}
