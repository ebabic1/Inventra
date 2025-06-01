package ba.unsa.etf.nwt.inventra.notification_service.dto;

import lombok.Data;

@Data
public class LowStockNotificationDTO {
    private String articleName;
    private int quantity;
}