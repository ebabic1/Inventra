package ba.unsa.etf.nwt.inventra.reporting_service.dto;

import ba.unsa.etf.nwt.inventra.reporting_service.model.ReportType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;
    @NotNull(message = "Type is required")
    private ReportType type;
}
