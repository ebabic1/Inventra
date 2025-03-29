package ba.unsa.etf.nwt.inventra.reporting_service.controller;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.ReportDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<ReportDTO> create(@RequestBody ReportDTO reportDTO) {
        ReportDTO createdReport = reportService.createReport(reportDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdReport);
    }

//    @GetMapping
//    public ResponseEntity<List<ReportDTO>> getAll() {
//        List<Report> Reports = repository.findAll();
//        List<ReportDTO> ReportDTOs = Reports.stream()
//                .map(reportMapper::reportToReportDTO)
//                .toList();
//        return ResponseEntity.ok(ReportDTOs);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ReportDTO> getById(@PathVariable Long id) {
//        Optional<Report> Report = repository.findById(id);
//        return Report.map(value -> ResponseEntity.ok(reportMapper.reportToReportDTO(value)))
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ReportDTO> update(@PathVariable Long id, @RequestBody ReportDTO reportDTO) {
//        Optional<Report> optionalReport = repository.findById(id);
//        if (optionalReport.isPresent()) {
//            Report Report = optionalReport.get();
//            Report.setUserId(reportDTO.getUserId());
//            Report.setGeneratedAt(reportDTO.getGeneratedAt());
//            Report updatedReport = repository.save(Report);
//            return ResponseEntity.ok(reportMapper.reportToReportDTO(updatedReport));
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        if (repository.existsById(id)) {
//            repository.deleteById(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
}