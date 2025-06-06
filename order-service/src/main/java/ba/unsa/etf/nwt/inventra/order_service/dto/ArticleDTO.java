package ba.unsa.etf.nwt.inventra.order_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleDTO {
    private Long id;
    private String name;
    private Double price;
}
