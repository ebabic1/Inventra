package ba.unsa.etf.nwt.inventra.reporting_service.mapper;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.OrderReportDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.model.OrderReport;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderReportMapper {

    OrderReportDTO orderReportToOrderReportDTO(OrderReport orderReport);

    OrderReport orderReportDTOToOrderReport(OrderReportDTO orderReportDTO);
}