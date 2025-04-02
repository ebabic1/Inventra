package ba.unsa.etf.nwt.inventra.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WarehouseDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Zip code is required")
    @Pattern(regexp = "\\d{5}", message = "Zip code must be 5 digits")
    private String zipCode;
}
