package ba.unsa.etf.nwt.inventra.reporting_service.controller;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.mapper.ArticleMapper;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Article;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.ArticleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleRepository repository;
    private final ArticleMapper articleMapper;

    ArticleController(ArticleRepository repository, ArticleMapper articleMapper) {
        this.repository = repository;
        this.articleMapper = articleMapper;
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> create(@RequestBody ArticleDTO articleDTO) {
        Article article = articleMapper.articleDTOToArticle(articleDTO);
        Article savedArticle = repository.save(article);
        return ResponseEntity.ok(articleMapper.articleToArticleDTO(savedArticle));
    }

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAll() {
        List<Article> articles = repository.findAll();
        List<ArticleDTO> articleDTOs = articles.stream()
                .map(articleMapper::articleToArticleDTO)
                .toList();
        return ResponseEntity.ok(articleDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getById(@PathVariable Long id) {
        Optional<Article> article = repository.findById(id);
        return article.map(value -> ResponseEntity.ok(articleMapper.articleToArticleDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> update(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        Optional<Article> optionalArticle = repository.findById(id);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            article.setName(articleDTO.getName());
            article.setPrice(articleDTO.getPrice());
            article.setCategory(articleDTO.getCategory());
            Article updatedArticle = repository.save(article);
            return ResponseEntity.ok(articleMapper.articleToArticleDTO(updatedArticle));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}