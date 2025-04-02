package ba.unsa.etf.nwt.inventra.order_service.mapper;

import ba.unsa.etf.nwt.inventra.order_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.order_service.model.Article;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    Article toEntity(ArticleDTO articleDTO);

    ArticleDTO toDTO(Article article);
}
