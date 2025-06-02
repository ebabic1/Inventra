package ba.unsa.etf.nwt.inventra.reporting_service.service;

import ba.unsa.etf.nwt.inventra.reporting_service.model.Article;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional
    public Article create(Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    public Article update(Long id, Article article) {
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found with id: " + id));

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
}
