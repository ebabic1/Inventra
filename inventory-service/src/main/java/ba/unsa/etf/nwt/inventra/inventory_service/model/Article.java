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
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private LocalDate expiryDate;
    private double price;
    private String category;
    private String description;
    private String image;
    private boolean excludeFromReports;
    private boolean notifyLowStock;
    private boolean notifyExpiryDate;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
}
