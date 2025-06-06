package ba.unsa.etf.nwt.inventra.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    @NotBlank(message = "Supplier name is required")
    @Size(max = 100, message = "Supplier name cannot be longer than 100 characters")
    private String name;
}
