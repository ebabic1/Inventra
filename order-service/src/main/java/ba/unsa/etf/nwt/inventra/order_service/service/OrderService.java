package ba.unsa.etf.nwt.inventra.order_service.service;

import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.repository.OrderRepository;
import ba.unsa.etf.nwt.inventra.order_service.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public List<Order> createBatch(List<Order> orders) {
        for (Order order : orders) {
            if (!supplierRepository.existsById(order.getSupplier().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier not found with ID: " + order.getSupplier().getId());
            }
        }
        return orderRepository.saveAll(orders);
    }

    @Transactional
    public Order create(Order order) {
        if (!supplierRepository.existsById(order.getSupplier().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier not found with ID: " + order.getSupplier().getId());
        }
        return orderRepository.save(order);
    }

    @Transactional
    public Order update(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + id));
        if (!supplierRepository.existsById(order.getSupplier().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier not found with ID: " + order.getSupplier().getId());
        }
        order.setId(existingOrder.getId());
        return orderRepository.save(order);
    }

    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Transactional
    public Order patch(Long id, Order orderUpdates) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + id));
        // TODO: Add more
        if (orderUpdates.getStatus() != null) {
            existingOrder.setStatus(orderUpdates.getStatus());
        }
        return orderRepository.save(existingOrder);
    }
}

