package ba.unsa.etf.nwt.inventra.inventory_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LowStockNotificationDTO {
    private Long id;
    private String name;
    private String category;
    private int quantity;
}
