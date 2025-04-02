package ba.unsa.etf.nwt.inventra.order_service.controller;

import ba.unsa.etf.nwt.inventra.order_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDTO;
import ba.unsa.etf.nwt.inventra.order_service.mapper.ArticleMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.Article;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.service.ArticleService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Article> pagedResult = articleService.findAll(pageable);

        List<ArticleDTO> articles = pagedResult.getContent()
                .stream()
                .map(articleMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(
            @Parameter(description = "ID of the article to be retrieved") @PathVariable Long id) {
        return articleService.findById(id)
                .map(articleMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(
            @Valid @RequestBody ArticleDTO articleDTO) {
        Article article = articleMapper.toEntity(articleDTO);
        Article createdArticle = articleService.create(article);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(articleMapper.toDTO(createdArticle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(
            @Parameter(description = "ID of the article to be updated") @PathVariable Long id,
            @Valid @RequestBody ArticleDTO articleDTO) {
        Article article = articleMapper.toEntity(articleDTO);
        Article updatedArticle = articleService.update(id, article);
        return ResponseEntity.ok(articleMapper.toDTO(updatedArticle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(
            @Parameter(description = "ID of the article to be deleted") @PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
