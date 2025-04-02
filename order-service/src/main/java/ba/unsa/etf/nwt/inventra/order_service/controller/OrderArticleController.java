package ba.unsa.etf.nwt.inventra.order_service.controller;

import ba.unsa.etf.nwt.inventra.order_service.dto.OrderArticleDTO;
import ba.unsa.etf.nwt.inventra.order_service.mapper.OrderArticleMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.OrderArticle;
import ba.unsa.etf.nwt.inventra.order_service.service.OrderArticleService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-articles")
@RequiredArgsConstructor
public class OrderArticleController {

    private final OrderArticleService orderArticleService;
    private final OrderArticleMapper orderArticleMapper;

    @GetMapping
    public ResponseEntity<List<OrderArticleDTO>> getAllOrderArticles() {
        List<OrderArticle> orderArticles = orderArticleService.findAll();
        return ResponseEntity.ok(orderArticles.stream()
                .map(orderArticleMapper::toDTO)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderArticleDTO> getOrderArticleById(
            @Parameter(description = "ID of the order-article to be retrieved") @PathVariable Long id) {
        return orderArticleService.findById(id)
                .map(orderArticleMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderArticleDTO> createOrderArticle(
            @Valid @RequestBody OrderArticleDTO orderArticleDTO) {
        OrderArticle orderArticle = orderArticleMapper.toEntity(orderArticleDTO);
        OrderArticle createdOrderArticle = orderArticleService.create(orderArticle);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderArticleMapper.toDTO(createdOrderArticle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderArticleDTO> updateOrderArticle(
            @Parameter(description = "ID of the order-article to be updated") @PathVariable Long id,
            @Valid @RequestBody OrderArticleDTO orderArticleDTO) {
        OrderArticle orderArticle = orderArticleMapper.toEntity(orderArticleDTO);
        OrderArticle updatedOrderArticle = orderArticleService.update(id, orderArticle);
        return ResponseEntity.ok(orderArticleMapper.toDTO(updatedOrderArticle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderArticle(
            @Parameter(description = "ID of the order-article to be deleted") @PathVariable Long id) {
        orderArticleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
