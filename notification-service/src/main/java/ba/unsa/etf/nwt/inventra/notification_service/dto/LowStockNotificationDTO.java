package ba.unsa.etf.nwt.inventra.notification_service.dto;

import lombok.Data;

@Data
public class LowStockNotificationDTO {
    private Long id;
    private String name;
    private int quantity;
    private String category;
}