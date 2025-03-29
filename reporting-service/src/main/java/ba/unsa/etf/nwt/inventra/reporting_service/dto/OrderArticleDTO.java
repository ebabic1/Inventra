package ba.unsa.etf.nwt.inventra.reporting_service.dto;

import ba.unsa.etf.nwt.inventra.reporting_service.model.Article;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderArticleDTO {
    private Order order;
    private Article article;
    private int quantity;
}

