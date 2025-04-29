package ba.unsa.etf.nwt.inventra.order_service.dto;

import lombok.Data;

@Data
public class LocationResponseDTO {
    private Long id;
    private String name;
    private Boolean isCapacityFull;
}
