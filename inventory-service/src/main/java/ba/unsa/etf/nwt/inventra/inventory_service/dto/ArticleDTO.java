package ba.unsa.etf.nwt.inventra.inventory_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ArticleDTO {
    private Long id;

    @NotBlank(message = "Article name is required")
    @Size(max = 100, message = "Article name cannot be longer than 100 characters")
    private String name;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @FutureOrPresent(message = "Expiry date must be in the future or present")
    private LocalDate expiryDate;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private Double price;

    @Size(max = 50, message = "Category cannot be longer than 50 characters")
    private String category;

    @Size(max = 500, message = "Description cannot be longer than 500 characters")
    private String description;

    private String image;

    private Boolean excludeFromReports;
    private Boolean notifyLowStock;
    private Boolean notifyExpiryDate;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Location ID is required")
    private Long locationId;
}