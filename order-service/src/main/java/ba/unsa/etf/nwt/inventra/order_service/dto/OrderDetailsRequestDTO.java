package ba.unsa.etf.nwt.inventra.order_service.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderDetailsRequestDTO {

    @NotBlank(message = "Order name is required")
    private String name;

    @NotNull(message = "Order date is required")
    @FutureOrPresent(message = "Order date must be today or in the future")
    private LocalDate orderDate;

    @NotNull(message = "Delivery date is required")
    @FutureOrPresent(message = "Delivery date must be today or in the future")
    private LocalDate deliveryDate;

    private String invoice;

    private String purchaseOrder;

    @Size(max = 500, message = "Note cannot exceed 500 characters")
    private String note;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotEmpty(message = "Articles list cannot be empty")
    private List<ArticleItem> articles;

    @Getter
    @Setter
    public static class ArticleItem {
        @NotNull(message = "Article ID is required")
        private Long articleId;

        @NotNull(message = "Price is required")
        private Double price;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        @NotNull(message = "Location ID is required")
        private Long locationId;
    }
}
