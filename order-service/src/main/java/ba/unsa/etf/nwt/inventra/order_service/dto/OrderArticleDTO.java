package ba.unsa.etf.nwt.inventra.order_service.dto;

import lombok.Data;

@Data
public class OrderArticleDTO {
    private Long orderId;
    private Long articleId;
    private Integer quantity;
}

