package ba.unsa.etf.nwt.inventra.inventory_service.controller;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.mapper.ArticleMapper;
import ba.unsa.etf.nwt.inventra.inventory_service.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @Operation(summary = "Batch insert articles", description = "Insert multiple articles in a single request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully inserted articles"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/batch")
    public ResponseEntity<List<ArticleDTO>> createArticlesBatch(@Valid @RequestBody List<ArticleDTO> articleDTOs) {
        List<Article> articles = articleDTOs.stream()
                .map(articleMapper::toEntity)
                .collect(Collectors.toList());

        List<Article> createdArticles = articleService.createBatch(articles);

        List<ArticleDTO> responseDTOs = createdArticles.stream()
                .map(articleMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTOs);
    }

    @Operation(summary = "Partially update an article", description = "Update only specific fields of an existing article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the article"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ArticleDTO> patchArticle(
            @Parameter(description = "ID of the article to be patched") @PathVariable Long id,
            @RequestBody ArticleDTO articleDTO) {
        Article articleUpdates = articleMapper.toEntity(articleDTO);
        Article updatedArticle = articleService.patchArticle(id, articleUpdates);
        return ResponseEntity.ok(articleMapper.toDTO(updatedArticle));
    }

    @Operation(summary = "Get all articles", description = "Retrieve a list of all articles in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of articles")
    })

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

    @Operation(summary = "Get article by ID", description = "Retrieve a specific article by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the article"),
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

    @Operation(summary = "Create a new article", description = "Create a new article and store it in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the article"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
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

    @Operation(summary = "Update an existing article", description = "Update the details of an existing article by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the article"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
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

    @Operation(summary = "Delete an article", description = "Delete an article from the system by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the article"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(
            @Parameter(description = "ID of the article to be deleted") @PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
