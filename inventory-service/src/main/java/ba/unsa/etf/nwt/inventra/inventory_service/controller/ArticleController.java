package ba.unsa.etf.nwt.inventra.inventory_service.controller;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.mapper.ArticleMapper;
import ba.unsa.etf.nwt.inventra.inventory_service.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<Article> articles = articleService.findAll();
        return ResponseEntity.ok(articles.stream()
                .map(articleMapper::toDTO)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        return articleService.findById(id)
                .map(articleMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        Article article = articleMapper.toEntity(articleDTO);
        Article createdArticle = articleService.create(article);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(articleMapper.toDTO(createdArticle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody ArticleDTO articleDTO) {
        Article article = articleMapper.toEntity(articleDTO);
        Article updatedArticle = articleService.update(id, article);
        return ResponseEntity.ok(articleMapper.toDTO(updatedArticle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
