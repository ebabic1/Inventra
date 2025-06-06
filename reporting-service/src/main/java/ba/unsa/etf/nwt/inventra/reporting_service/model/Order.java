package ba.unsa.etf.nwt.inventra.reporting_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "`order`")
public class Order {
    @Id
    private Long id;
    private LocalDateTime orderDate;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderArticle> orderArticles;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
