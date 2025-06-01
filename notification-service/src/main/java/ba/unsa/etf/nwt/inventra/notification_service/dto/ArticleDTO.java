package ba.unsa.etf.nwt.inventra.notification_service.dto;

import lombok.Data;

@Data
public class ArticleDTO {

    // TODO: Do we need constraints?

    private Long id;

    private String name;

    private Integer quantity;
}