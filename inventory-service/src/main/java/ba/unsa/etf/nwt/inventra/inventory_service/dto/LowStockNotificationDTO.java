package ba.unsa.etf.nwt.inventra.inventory_service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LowStockNotificationDTO {
    //TODO: Use mapper later and add id

    private String articleName;
    private int quantity;

    public LowStockNotificationDTO() {}

    public LowStockNotificationDTO(String articleName, int quantity) {
        this.articleName = articleName;
        this.quantity = quantity;
    }

}
