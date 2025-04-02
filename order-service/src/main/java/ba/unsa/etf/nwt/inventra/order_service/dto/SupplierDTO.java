package ba.unsa.etf.nwt.inventra.order_service.dto;

import ba.unsa.etf.nwt.inventra.order_service.model.SupplierCategory;
import lombok.*;

@Data
public class SupplierDTO {
    private String name;
    private String phone;
    private String email;
    private SupplierCategory category;
}

