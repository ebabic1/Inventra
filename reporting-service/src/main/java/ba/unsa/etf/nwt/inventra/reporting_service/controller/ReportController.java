package ba.unsa.etf.nwt.inventra.reporting_service.controller;

import ba.unsa.etf.nwt.inventra.reporting_service.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/orders")
    public ResponseEntity<byte[]> getOrderSummaryReport(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        byte[] pdfContent = reportService.generateOrderSummaryReport(start, end);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }

    @GetMapping("/most-ordered-articles")
    public ResponseEntity<byte[]>  generateArticleOrderedReport() {
        byte[] pdfContent = reportService.generateArticleOrderedReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=most_ordered_articles_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }

    @GetMapping("/test-loadbalancing")
    public ResponseEntity<String> testLoadBalancing() {
        try {
            String instancePort = reportService.getInstancePort();
            return ResponseEntity.ok(instancePort);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while testing load balancing.");
        }
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