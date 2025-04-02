package ba.unsa.etf.nwt.inventra.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SupplierDTO {
    @NotBlank(message = "Supplier name is required")
    @Size(max = 100, message = "Supplier name cannot be longer than 100 characters")
    private String name;
}
