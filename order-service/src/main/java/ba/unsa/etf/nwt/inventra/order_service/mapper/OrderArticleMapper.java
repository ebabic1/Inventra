package ba.unsa.etf.nwt.inventra.order_service.mapper;

import ba.unsa.etf.nwt.inventra.order_service.dto.OrderArticleDTO;
import ba.unsa.etf.nwt.inventra.order_service.model.Article;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.model.OrderArticle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderArticleMapper {
    @Mapping(target = "order", source = "orderId", qualifiedByName = "idToOrder")
    @Mapping(target = "article", source = "articleId", qualifiedByName = "idToArticle")
    OrderArticle toEntity(OrderArticleDTO ordeArticleDTO);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "articleId", source = "article.id")
    OrderArticleDTO toDTO(OrderArticle orderArticle);

    @Named("idToOrder")
    default Order idToOrder(Long id) {
        if (id == null) return null;
        Order order = new Order();
        order.setId(id);
        return order;
    }

    @Named("idToArticle")
    default Article idToArticle(Long id) {
        if (id == null) return null;
        Article article = new Article();
        article.setId(id);
        return article;
    }
}
