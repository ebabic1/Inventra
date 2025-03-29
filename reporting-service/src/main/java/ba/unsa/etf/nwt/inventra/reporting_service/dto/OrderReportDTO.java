package ba.unsa.etf.nwt.inventra.reporting_service.dto;

import ba.unsa.etf.nwt.inventra.reporting_service.model.OrderArticle;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderReportDTO {
    private Report report;
    private OrderArticle orderArticle;
}
