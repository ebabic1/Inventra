package ba.unsa.etf.nwt.inventra.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LocationDTO {
    private Long id;

    @NotBlank(message = "Location name is required")
    @Size(max = 100, message = "Location name cannot be longer than 100 characters")
    private String name;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;
}

