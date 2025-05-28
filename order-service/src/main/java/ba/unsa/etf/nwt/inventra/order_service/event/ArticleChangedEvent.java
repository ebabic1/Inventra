package ba.unsa.etf.nwt.inventra.order_service.event;

import lombok.Data;

@Data
public class ArticleChangedEvent {
    private Long id;
    private String name;
    private Double price;
    private String category;
    private Integer quantity;
    private String action;
}