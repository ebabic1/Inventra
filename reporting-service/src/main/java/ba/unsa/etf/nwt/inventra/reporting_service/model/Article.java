package ba.unsa.etf.nwt.inventra.reporting_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private String category;

    @OneToMany(mappedBy = "article")
    @JsonIgnore
    private List<OrderArticle> orderArticles;
}