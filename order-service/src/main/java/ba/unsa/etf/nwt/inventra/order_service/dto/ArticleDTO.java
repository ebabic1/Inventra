package ba.unsa.etf.nwt.inventra.order_service.dto;

import lombok.Data;

@Data
public class ArticleDTO {
    private Long id;
    private String name;
    private double price;
}
