package ba.unsa.etf.nwt.inventra.inventory_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "Article.detail",
        attributeNodes = {
                @NamedAttributeNode("location")
        }
)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer quantity;
    private LocalDate expiryDate;
    private Double price;
    private String category;
    private String description;
    private String image;
    private boolean excludeFromReports;
    private boolean notifyLowStock;
    private boolean notifyExpiryDate;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
}
