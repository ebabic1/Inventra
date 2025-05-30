package ba.unsa.etf.nwt.inventra.inventory_service;

import ba.unsa.etf.nwt.inventra.inventory_service.controller.ArticleController;
import ba.unsa.etf.nwt.inventra.inventory_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.mapper.ArticleMapper;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Location;
import ba.unsa.etf.nwt.inventra.inventory_service.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.any;

@WebMvcTest(ArticleController.class)
@ExtendWith(MockitoExtension.class)
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleService articleService;

    @MockitoBean
    private ArticleMapper articleMapper;

    @Test
    void getAllArticles_ShouldReturnList() throws Exception {
        Article article1 = new Article();
        article1.setId(1L);
        article1.setName("New Article 1");

        Article article2 = new Article();
        article2.setId(2L);
        article2.setName("New Article 2");

        when(articleService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(article1, article2)));

        when(articleMapper.toDTO(any())).thenAnswer(invocation -> {
            Article a = invocation.getArgument(0);
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setName(a.getName());
            return articleDTO;
        });

        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getArticleById_ShouldReturnArticle() throws Exception {
        Article article = new Article();
        article.setId(1L);
        article.setName("Test Article");

        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setName("Test Article");

        when(articleService.findById(1L)).thenReturn(Optional.of(article));
        when(articleMapper.toDTO(article)).thenReturn(articleDTO);

        mockMvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Article"));
    }

    @Test
    void getArticleById_ShouldReturnNotFound() throws Exception {
        when(articleService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/articles/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteArticle_ShouldReturnNoContent() throws Exception {
        doNothing().when(articleService).delete(1L);

        mockMvc.perform(delete("/api/articles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createArticle_ShouldReturnCreatedArticle() throws Exception {
        ArticleDTO inputArticleDTO = new ArticleDTO();
        inputArticleDTO.setName("New Test Article");
        inputArticleDTO.setQuantity(10);
        inputArticleDTO.setPrice(100.0);
        inputArticleDTO.setSupplierId(1L);
        inputArticleDTO.setLocationId(2L);

        Article createdArticle = new Article();
        createdArticle.setId(1L);
        createdArticle.setName("New Test Article");
        createdArticle.setQuantity(10);
        createdArticle.setPrice(100.0);
        createdArticle.setSupplierId(1L);

        Location location = new Location();
        location.setId(2L);
        createdArticle.setLocation(location);

        ArticleDTO outputArticleDTO = new ArticleDTO();
        outputArticleDTO.setName("New Test Article");
        outputArticleDTO.setQuantity(10);
        outputArticleDTO.setPrice(100.0);

        when(articleMapper.toEntity(any(ArticleDTO.class))).thenReturn(createdArticle);
        when(articleService.create(any(Article.class))).thenReturn(createdArticle);
        when(articleMapper.toDTO(any(Article.class))).thenReturn(outputArticleDTO);

        mockMvc.perform(post("/api/articles")
                        .contentType("application/json")
                        .content("""
                            {
                              "name": "New Test Article",
                              "quantity": 10,
                              "price": 100.0,
                              "supplierId": 1,
                              "locationId": 2
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Test Article"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.price").value(100.0));
    }

    @Test
    void updateArticle_ShouldReturnUpdatedArticle() throws Exception {
        Long articleId = 1L;

        ArticleDTO inputArticleDTO = new ArticleDTO();
        inputArticleDTO.setName("Updated Test Article");
        inputArticleDTO.setQuantity(20);
        inputArticleDTO.setPrice(200.0);
        inputArticleDTO.setSupplierId(2L);
        inputArticleDTO.setLocationId(3L);

        Article updatedArticle = new Article();
        updatedArticle.setId(articleId);
        updatedArticle.setName("Updated Test Article");
        updatedArticle.setQuantity(20);
        updatedArticle.setPrice(200.0);
        updatedArticle.setSupplierId(2L);

        Location location = new Location();
        location.setId(3L);
        updatedArticle.setLocation(location);

        ArticleDTO outputArticleDTO = new ArticleDTO();
        outputArticleDTO.setName("Updated Test Article");
        outputArticleDTO.setQuantity(20);
        outputArticleDTO.setPrice(200.0);

        when(articleMapper.toEntity(any(ArticleDTO.class))).thenReturn(updatedArticle);
        when(articleService.update(eq(articleId), any(Article.class))).thenReturn(updatedArticle);
        when(articleMapper.toDTO(any(Article.class))).thenReturn(outputArticleDTO);

        mockMvc.perform(put("/api/articles/{id}", articleId)
                        .contentType("application/json")
                        .content("""
                            {
                              "name": "Updated Test Article",
                              "quantity": 20,
                              "price": 200.0,
                              "supplierId": 2,
                              "locationId": 3
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Test Article"))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.price").value(200.0));
    }

    @Test
    void getArticleById_ShouldReturnArticle_WhenArticleExists() throws Exception {
        Long articleId = 1L;

        Article article = new Article();
        article.setId(articleId);
        article.setName("Test Article");

        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setName("Test Article");

        when(articleService.findById(articleId)).thenReturn(Optional.of(article));
        when(articleMapper.toDTO(article)).thenReturn(articleDTO);

        mockMvc.perform(get("/api/articles/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Article"));
    }

    @Test
    void patchArticle_ShouldReturnUpdatedArticle_WhenPatchIsSuccessful() throws Exception {
        Long articleId = 1L;

        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setName("Updated Name");

        Article articleUpdates = new Article();
        articleUpdates.setName("Updated Name");

        Article updatedArticle = new Article();
        updatedArticle.setId(articleId);
        updatedArticle.setName("Updated Name");

        ArticleDTO updatedArticleDTO = new ArticleDTO();
        updatedArticleDTO.setName("Updated Name");

        when(articleMapper.toEntity(any(ArticleDTO.class))).thenReturn(articleUpdates);
        when(articleService.patchArticle(eq(articleId), any(Article.class))).thenReturn(updatedArticle);
        when(articleMapper.toDTO(any(Article.class))).thenReturn(updatedArticleDTO);

        mockMvc.perform(patch("/api/articles/{id}", articleId)
                        .contentType("application/json")
                        .content("{\"name\": \"Updated Name\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void patchArticle_ShouldReturnNotFound_WhenArticleDoesNotExist() throws Exception {
        Long articleId = 1L;

        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setName("Updated Name");

        when(articleMapper.toEntity(any(ArticleDTO.class))).thenReturn(new Article());
        when(articleService.patchArticle(eq(articleId), any(Article.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(patch("/api/articles/{id}", articleId)
                        .contentType("application/json")
                        .content("{\"name\": \"Updated Name\"}"))
                .andExpect(status().isNotFound());
    }
}
