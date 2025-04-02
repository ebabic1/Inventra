package ba.unsa.etf.nwt.inventra.reporting_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private String name;
    private Double price;
    private String category;
    private Long totalQuantityOrdered;
}
