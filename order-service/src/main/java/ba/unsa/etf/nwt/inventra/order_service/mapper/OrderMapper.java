package ba.unsa.etf.nwt.inventra.order_service.mapper;

import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDetailsRequestDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDetailsResponseDTO;
import ba.unsa.etf.nwt.inventra.order_service.model.Article;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.model.OrderArticle;
import ba.unsa.etf.nwt.inventra.order_service.model.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "supplier", source = "supplierId", qualifiedByName = "idToSupplier")
    Order toEntity(OrderDTO dto);

    @Mapping(target = "supplierId", source = "supplier.id")
    OrderDTO toDTO(Order order);

    @Named("idToSupplier")
    default Supplier idToSupplier(Long id) {
        if (id == null) return null;
        Supplier supplier = new Supplier();
        supplier.setId(id);
        return supplier;
    }

    @Named("fromOrderDetails")
    default Order fromOrderDetails(OrderDetailsRequestDTO request, Supplier supplier) {
        Order order = new Order();
        order.setName(request.getName());
        order.setOrderDate(request.getOrderDate());
        order.setDeliveryDate(request.getDeliveryDate());
        order.setInvoice(request.getInvoice());
        order.setPurchaseOrder(request.getPurchaseOrder());
        order.setNote(request.getNote());
        order.setUserId(request.getUserId());
        order.setSupplier(supplier);

        List<OrderArticle> orderArticles = new ArrayList<>();
        for (OrderDetailsRequestDTO.ArticleItem item : request.getArticles()) {
            OrderArticle oa = new OrderArticle();

            Article article = new Article();
            article.setId(item.getArticleId());

            oa.setArticle(article);
            oa.setQuantity(item.getQuantity());
            oa.setOrder(order);

            orderArticles.add(oa);
        }

        order.setOrderArticles(orderArticles);
        return order;
    }

    default OrderDetailsResponseDTO toDetailsResponseDTO(Order order) {
        if (order == null) return null;

        OrderDetailsResponseDTO dto = new OrderDetailsResponseDTO();
        dto.setName(order.getName());
        dto.setOrderDate(order.getOrderDate());
        dto.setDeliveryDate(order.getDeliveryDate());
        dto.setInvoice(order.getInvoice());
        dto.setPurchaseOrder(order.getPurchaseOrder());
        dto.setNote(order.getNote());
        dto.setUserId(order.getUserId());
        dto.setSupplierId(order.getSupplier() != null ? order.getSupplier().getId() : null);

        if (order.getOrderArticles() != null) {
            List<OrderDetailsResponseDTO.ArticleItem> articles = order.getOrderArticles()
                    .stream()
                    .map(oa -> {
                        OrderDetailsResponseDTO.ArticleItem articleItem = new OrderDetailsResponseDTO.ArticleItem();
                        articleItem.setQuantity(oa.getQuantity());
                        articleItem.setName(oa.getArticle().getName());
                        articleItem.setPrice(oa.getArticle().getPrice());
                        return articleItem;
                    })
                    .collect(Collectors.toList());
            dto.setArticles(articles);
        }

        return dto;
    }
}
