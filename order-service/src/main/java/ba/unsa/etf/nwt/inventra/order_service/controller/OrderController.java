package ba.unsa.etf.nwt.inventra.order_service.controller;

import ba.unsa.etf.nwt.inventra.order_service.mapper.OrderMapper;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDTO;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Operation(summary = "Batch insert articles", description = "Insert multiple articles in a single request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully inserted articles"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/batch")
    public ResponseEntity<List<OrderDTO>> createArticlesBatch(@Valid @RequestBody List<OrderDTO> orderDTOs) {
        List<Order> articles = orderDTOs.stream()
                .map(orderMapper::toEntity)
                .collect(Collectors.toList());

        List<Order> createdArticles = orderService.createBatch(articles);

        List<OrderDTO> responseDTOs = createdArticles.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTOs);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Order> pagedResult = orderService.findAll(pageable);

        List<OrderDTO> orders = pagedResult.getContent()
                .stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(orderMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderDTO> patchOrder(@PathVariable Long id, @RequestBody OrderDTO orderUpdates) {
        Order updatedOrder = orderService.patch(id, orderMapper.toEntity(orderUpdates));
        return ResponseEntity.ok(orderMapper.toDTO(updatedOrder));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        Order newOrder = orderService.create(order);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderMapper.toDTO(newOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        Order updatedOrder= orderService.update(id, order);
        return ResponseEntity.ok(orderMapper.toDTO(updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
