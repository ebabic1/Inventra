package ba.unsa.etf.nwt.inventra.reporting_service.mapper;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {

    ArticleDTO articleToArticleDTO(Article article);

    Article articleDTOToArticle(ArticleDTO articleDTO);
}