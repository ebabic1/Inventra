package ba.unsa.etf.nwt.inventra.reporting_service.controller;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.OrderArticleDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.mapper.OrderArticleMapper;
import ba.unsa.etf.nwt.inventra.reporting_service.model.OrderArticle;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.OrderArticleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-articles")
public class OrderArticleController {

    private final OrderArticleRepository repository;
    private final OrderArticleMapper orderArticleMapper;

    OrderArticleController(OrderArticleRepository repository, OrderArticleMapper orderArticleMapper) {
        this.repository = repository;
        this.orderArticleMapper = orderArticleMapper;
    }

    @PostMapping
    public ResponseEntity<OrderArticleDTO> create(@RequestBody OrderArticleDTO orderArticleDTO) {
        OrderArticle OrderArticle = orderArticleMapper.orderArticleDTOToOrderArticle(orderArticleDTO);
        OrderArticle savedOrderArticle = repository.save(OrderArticle);
        return ResponseEntity.ok(orderArticleMapper.orderArticleToOrderArticleDTO(savedOrderArticle));
    }

    @GetMapping
    public ResponseEntity<List<OrderArticleDTO>> getAll() {
        List<OrderArticle> OrderArticles = repository.findAll();
        List<OrderArticleDTO> OrderArticleDTOs = OrderArticles.stream()
                .map(orderArticleMapper::orderArticleToOrderArticleDTO)
                .toList();
        return ResponseEntity.ok(OrderArticleDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderArticleDTO> getById(@PathVariable Long id) {
        Optional<OrderArticle> OrderArticle = repository.findById(id);
        return OrderArticle.map(value -> ResponseEntity.ok(orderArticleMapper.orderArticleToOrderArticleDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderArticleDTO> update(@PathVariable Long id, @RequestBody OrderArticleDTO orderArticleDTO) {
        Optional<OrderArticle> optionalOrderArticle = repository.findById(id);
        if (optionalOrderArticle.isPresent()) {
            OrderArticle OrderArticle = optionalOrderArticle.get();
            OrderArticle.setQuantity(orderArticleDTO.getQuantity());
            OrderArticle updatedOrderArticle = repository.save(OrderArticle);
            return ResponseEntity.ok(orderArticleMapper.orderArticleToOrderArticleDTO(updatedOrderArticle));
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