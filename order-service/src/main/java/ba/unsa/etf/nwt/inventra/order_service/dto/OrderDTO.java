package ba.unsa.etf.nwt.inventra.order_service.dto;

import ba.unsa.etf.nwt.inventra.order_service.model.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderDTO {
    private String name;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private OrderStatus status;
    private String invoice;
    private String purchaseOrder;
    private String note;
    private Long userId;
    private Long supplierId;
}
