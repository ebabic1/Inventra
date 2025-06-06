package ba.unsa.etf.nwt.events;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OrderArticle {
    private Long articleId;
    private String articleName;
    private Double articlePrice;
    private String articleCategory;
    private Integer quantity;
}