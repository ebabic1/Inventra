package ba.unsa.etf.nwt.inventra.reporting_service.controller;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.OrderReportDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.mapper.OrderReportMapper;
import ba.unsa.etf.nwt.inventra.reporting_service.model.OrderReport;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.OrderReportRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-reports")
public class OrderReportController {

    private final OrderReportRepository repository;
    private final OrderReportMapper orderReportMapper;

    OrderReportController(OrderReportRepository repository, OrderReportMapper orderReportMapper) {
        this.repository = repository;
        this.orderReportMapper = orderReportMapper;
    }

    @PostMapping
    public ResponseEntity<OrderReportDTO> create(@RequestBody OrderReportDTO orderReportDTO) {
        OrderReport OrderReport = orderReportMapper.orderReportDTOToOrderReport(orderReportDTO);
        OrderReport savedOrderReport = repository.save(OrderReport);
        return ResponseEntity.ok(orderReportMapper.orderReportToOrderReportDTO(savedOrderReport));
    }

    @GetMapping
    public ResponseEntity<List<OrderReportDTO>> getAll() {
        List<OrderReport> OrderReports = repository.findAll();
        List<OrderReportDTO> OrderReportDTOs = OrderReports.stream()
                .map(orderReportMapper::orderReportToOrderReportDTO)
                .toList();
        return ResponseEntity.ok(OrderReportDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderReportDTO> getById(@PathVariable Long id) {
        Optional<OrderReport> OrderReport = repository.findById(id);
        return OrderReport.map(value -> ResponseEntity.ok(orderReportMapper.orderReportToOrderReportDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderReportDTO> update(@PathVariable Long id, @RequestBody OrderReportDTO orderReportDTO) {
        Optional<OrderReport> optionalOrderReport = repository.findById(id);
        if (optionalOrderReport.isPresent()) {
            OrderReport OrderReport = optionalOrderReport.get();
            OrderReport updatedOrderReport = repository.save(OrderReport);
            return ResponseEntity.ok(orderReportMapper.orderReportToOrderReportDTO(updatedOrderReport));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}