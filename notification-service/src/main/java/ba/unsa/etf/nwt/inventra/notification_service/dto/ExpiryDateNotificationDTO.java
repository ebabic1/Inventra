package ba.unsa.etf.nwt.inventra.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpiryDateNotificationDTO {
    private String name;
    private String expiryDate;
    private Long id;
    private String category;
}
