package ba.unsa.etf.nwt.inventra.inventory_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "supplier",  cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Article> articles;
}
