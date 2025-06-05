package ba.unsa.etf.nwt.inventra.inventory_service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LowStockNotificationDTO {
    //TODO: Use mapper later and add id

    private Long id;
    private String name;
    private String category;
    private int quantity;

    public LowStockNotificationDTO() {}

    public LowStockNotificationDTO(String name, int quantity, Long id, String category) {
        this.name = name;
        this.quantity = quantity;
        this.id = id;
        this.category = category;
    }

}
