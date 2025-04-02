package ba.unsa.etf.nwt.inventra.inventory_service;

import ba.unsa.etf.nwt.inventra.inventory_service.controller.ArticleController;
import ba.unsa.etf.nwt.inventra.inventory_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.mapper.ArticleMapper;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

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
}
