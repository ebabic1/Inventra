package ba.unsa.etf.nwt.inventra.reporting_service.mapper;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.OrderArticleDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.model.OrderArticle;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderArticleMapper {

    OrderArticleDTO orderArticleToOrderArticleDTO(OrderArticle orderArticle);
    OrderArticle orderArticleDTOToOrderArticle(OrderArticleDTO orderArticleDTO);
}