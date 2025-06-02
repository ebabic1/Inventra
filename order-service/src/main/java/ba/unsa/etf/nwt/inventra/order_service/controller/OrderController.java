package ba.unsa.etf.nwt.inventra.order_service.controller;

import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDetailsRequestDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDetailsResponseDTO;
import ba.unsa.etf.nwt.inventra.order_service.mapper.OrderMapper;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDTO;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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

    @Autowired
    private Environment environment;

    @Operation(summary = "Batch insert articles", description = "Insert multiple articles in a single request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully inserted articles"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/batch")
    public ResponseEntity<List<OrderDTO>> createArticlesBatch(@Valid @RequestBody List<OrderDetailsRequestDTO> detailsDTOs) {
        List<Order> createdArticles = orderService.createBatch(detailsDTOs);

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

    @Operation(summary = "Get order by ID", description = "Retrieve an order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsResponseDTO> getOrderById(@PathVariable Long id) {
        OrderDetailsResponseDTO order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderDTO> patchOrder(@PathVariable Long id, @RequestBody OrderDTO orderUpdates) {
        Order updatedOrder = orderService.patch(id, orderMapper.toEntity(orderUpdates));
        return ResponseEntity.ok(orderMapper.toDTO(updatedOrder));
    }

    @Operation(summary = "Mark order as FINISHED asynchronously", description = "Marks an order as FINISHED using Saga choreography and RabbitMQ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Order update initiated"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PostMapping("/{id}/finish")
    public ResponseEntity<String> markOrderAsFinished(@PathVariable Long id) {
        orderService.markAsFinished(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Order finish initiated.");
    }

    @Operation(summary = "Create a new order", description = "Create a new order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "503", description = "Inventory service unavailable")
    })
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody OrderDetailsRequestDTO detailsDTO) {

        Order newOrder = orderService.create(detailsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toDTO(newOrder));
    }

    @Operation(summary = "Update an existing order", description = "Update the details of an existing order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(
            @Parameter(description = "ID of the order to be updated") @PathVariable Long id,
            @RequestBody OrderDetailsRequestDTO detailsDTO) {
        Order updatedOrder = orderService.update(id, detailsDTO);
        return ResponseEntity.ok(orderMapper.toDTO(updatedOrder));
    }

    @Operation(summary = "Delete an order", description = "Delete an order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "ID of the order to be deleted") @PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Value("${server.port:unknown}")
    private String instancePort;

    @GetMapping("/instance-port")
    public ResponseEntity<String> getInstancePort() {
        return ResponseEntity.ok(instancePort);
    }
}
