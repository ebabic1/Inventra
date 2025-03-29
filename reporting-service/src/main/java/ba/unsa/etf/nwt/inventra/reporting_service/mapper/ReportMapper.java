package ba.unsa.etf.nwt.inventra.reporting_service.mapper;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.ReportDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReportMapper {

    ReportDTO reportToReportDTO(Report report);

    Report reportDTOToReport(ReportDTO reportDTO);
}