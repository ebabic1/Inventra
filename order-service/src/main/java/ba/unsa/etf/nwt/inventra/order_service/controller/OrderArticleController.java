package ba.unsa.etf.nwt.inventra.order_service.controller;

import ba.unsa.etf.nwt.inventra.order_service.dto.OrderArticleDTO;
import ba.unsa.etf.nwt.inventra.order_service.mapper.OrderArticleMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.OrderArticle;
import ba.unsa.etf.nwt.inventra.order_service.service.OrderArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all order articles", description = "Retrieve a list of all order articles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of order articles retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<OrderArticleDTO>> getAllOrderArticles() {
        List<OrderArticle> orderArticles = orderArticleService.findAll();
        return ResponseEntity.ok(orderArticles.stream()
                .map(orderArticleMapper::toDTO)
                .toList());
    }

    @Operation(summary = "Get order article by ID", description = "Retrieve an order article by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order article retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order article not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderArticleDTO> getOrderArticleById(
            @Parameter(description = "ID of the order-article to be retrieved") @PathVariable Long id) {
        return orderArticleService.findById(id)
                .map(orderArticleMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new order article", description = "Create a new order article.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order article created successfully")
    })
    @PostMapping
    public ResponseEntity<OrderArticleDTO> createOrderArticle(
            @Valid @RequestBody OrderArticleDTO orderArticleDTO) {
        OrderArticle orderArticle = orderArticleMapper.toEntity(orderArticleDTO);
        OrderArticle createdOrderArticle = orderArticleService.create(orderArticle);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderArticleMapper.toDTO(createdOrderArticle));
    }

    @Operation(summary = "Update an existing order article", description = "Update the details of an existing order article.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order article updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order article not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderArticleDTO> updateOrderArticle(
            @Parameter(description = "ID of the order-article to be updated") @PathVariable Long id,
            @Valid @RequestBody OrderArticleDTO orderArticleDTO) {
        OrderArticle orderArticle = orderArticleMapper.toEntity(orderArticleDTO);
        OrderArticle updatedOrderArticle = orderArticleService.update(id, orderArticle);
        return ResponseEntity.ok(orderArticleMapper.toDTO(updatedOrderArticle));
    }

    @Operation(summary = "Delete an order article", description = "Delete an order article by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order article deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order article not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderArticle(
            @Parameter(description = "ID of the order-article to be deleted") @PathVariable Long id) {
        orderArticleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
