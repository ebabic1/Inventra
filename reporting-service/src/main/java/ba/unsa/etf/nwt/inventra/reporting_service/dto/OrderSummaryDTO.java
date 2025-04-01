package ba.unsa.etf.nwt.inventra.reporting_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {
    private Long id;
    private LocalDateTime orderDate;
    private String articleName;
    private Integer quantity;
    private Double totalPrice;
}