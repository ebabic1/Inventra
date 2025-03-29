package ba.unsa.etf.nwt.inventra.reporting_service.service;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.ReportDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.mapper.ReportMapper;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Report;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public ReportService(ReportRepository reportRepository,
                         ReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    public ReportDTO createReport(ReportDTO reportDTO) {
        Report report = reportMapper.reportDTOToReport(reportDTO);
        report.setGeneratedAt(LocalDateTime.now());
        report.setUserId(reportDTO.getUserId());
        Report savedReport = reportRepository.save(report);
        return reportMapper.reportToReportDTO(savedReport);
    }
}