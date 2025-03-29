package ba.unsa.etf.nwt.inventra.reporting_service.controller;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.OrderDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.mapper.OrderMapper;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Order;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository repository;
    private final OrderMapper orderMapper;

    OrderController(OrderRepository repository, OrderMapper orderMapper) {
        this.repository = repository;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody OrderDTO orderDTO) {
        Order Order = orderMapper.orderDTOToOrder(orderDTO);
        Order savedOrder = repository.save(Order);
        return ResponseEntity.ok(orderMapper.orderToOrderDTO(savedOrder));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAll() {
        List<Order> Orders = repository.findAll();
        List<OrderDTO> OrderDTOs = Orders.stream()
                .map(orderMapper::orderToOrderDTO)
                .toList();
        return ResponseEntity.ok(OrderDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getById(@PathVariable Long id) {
        Optional<Order> Order = repository.findById(id);
        return Order.map(value -> ResponseEntity.ok(orderMapper.orderToOrderDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> update(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        Optional<Order> optionalOrder = repository.findById(id);
        if (optionalOrder.isPresent()) {
            Order Order = optionalOrder.get();
            Order.setOrderDate(orderDTO.getOrderDate());
            Order updatedOrder = repository.save(Order);
            return ResponseEntity.ok(orderMapper.orderToOrderDTO(updatedOrder));
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