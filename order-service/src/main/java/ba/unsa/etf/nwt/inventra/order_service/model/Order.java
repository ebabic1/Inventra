package ba.unsa.etf.nwt.inventra.order_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NamedEntityGraph(
        name = "Order.detail",
        attributeNodes = {
                @NamedAttributeNode("supplier"),
        }
)
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Order date cannot be null")
    @FutureOrPresent(message = "Order date must be today or in the future")
    private LocalDate orderDate;

    @NotNull(message = "Expiry date cannot be null")
    @FutureOrPresent(message = "Expiry date must be today or in the future")
    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String invoice;

    private String purchaseOrder;

    @Size(max = 500, message = "Note cannot exceed 500 characters")
    private String note;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderArticle> orderArticles;
}
