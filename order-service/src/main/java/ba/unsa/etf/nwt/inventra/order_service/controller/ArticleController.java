package ba.unsa.etf.nwt.inventra.order_service.controller;

import ba.unsa.etf.nwt.inventra.order_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.order_service.mapper.ArticleMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.Article;
import ba.unsa.etf.nwt.inventra.order_service.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @Operation(summary = "Get all articles", description = "Retrieve a list of all articles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of articles retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<Article> articles = articleService.findAll();
        return ResponseEntity.ok(articles.stream()
                .map(articleMapper::toDTO)
                .toList());
    }

    @Operation(summary = "Get article by ID", description = "Retrieve an article by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(
            @Parameter(description = "ID of the article to be retrieved") @PathVariable Long id) {
        return articleService.findById(id)
                .map(articleMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new article", description = "Create a new article.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Article created successfully")
    })
    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(
            @Valid @RequestBody ArticleDTO articleDTO) {
        Article article = articleMapper.toEntity(articleDTO);
        Article createdArticle = articleService.create(article);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(articleMapper.toDTO(createdArticle));
    }

    @Operation(summary = "Update an existing article", description = "Update the details of an existing article.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article updated successfully"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(
            @Parameter(description = "ID of the article to be updated") @PathVariable Long id,
            @Valid @RequestBody ArticleDTO articleDTO) {
        Article article = articleMapper.toEntity(articleDTO);
        Article updatedArticle = articleService.update(id, article);
        return ResponseEntity.ok(articleMapper.toDTO(updatedArticle));
    }

    @Operation(summary = "Delete an article", description = "Delete an article by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Article deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(
            @Parameter(description = "ID of the article to be deleted") @PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
