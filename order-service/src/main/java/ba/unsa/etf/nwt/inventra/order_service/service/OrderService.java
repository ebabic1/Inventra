package ba.unsa.etf.nwt.inventra.order_service.service;

import ba.unsa.etf.nwt.inventra.order_service.client.InventoryClient;
import ba.unsa.etf.nwt.inventra.order_service.dto.*;
import ba.unsa.etf.nwt.inventra.order_service.mapper.OrderMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.model.Supplier;
import ba.unsa.etf.nwt.inventra.order_service.repository.OrderRepository;
import ba.unsa.etf.nwt.inventra.order_service.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final OrderMapper orderMapper;
    private final InventoryClient inventoryClient;
    private final SystemEventsClient systemEventsClient;

    public Page<Order> findAll(Pageable pageable) {
        Page<Order> page = orderRepository.findAll(pageable);
        logEvent("GET_ALL", "Order", "SUCCESS");
        return page;
    }

    public OrderDetailsResponseDTO findById(Long id) {
        Order order = validateOrderExists("GET", id);
        logEvent("GET", "Order", "SUCCESS");
        return orderMapper.toDetailsResponseDTO(order);
    }

    @Transactional
    public List<Order> createBatch(List<OrderDetailsRequestDTO> detailsDTOs) {
        List<Order> orders = detailsDTOs.stream().map(requestDTO -> {
            Supplier supplier = validateSupplier("CREATE_BATCH", requestDTO.getSupplierId());

            for (OrderDetailsRequestDTO.ArticleItem item : requestDTO.getArticles()) {
                validateArticleAndLocation("CREATE_BATCH", item);
            }

            return orderMapper.fromOrderDetails(requestDTO, supplier);
        }).toList();
        logEvent("CREATE_BATCH", "Order", "SUCCESS");
        return orderRepository.saveAll(orders);
    }

    @Transactional
    public Order create(OrderDetailsRequestDTO detailsDTO) {
        Supplier supplier = validateSupplier("CREATE", detailsDTO.getSupplierId());

        for (OrderDetailsRequestDTO.ArticleItem item : detailsDTO.getArticles()) {
            validateArticleAndLocation("CREATE", item);
        }

        Order order = orderMapper.fromOrderDetails(detailsDTO, supplier);
        logEvent("CREATE", "Order", "SUCCESS");
        return orderRepository.save(order);
    }

    @Transactional
    public Order update(Long id, OrderDetailsRequestDTO detailsDTO) {
        Order existingOrder = validateOrderExists("UPDATE", id);

        Supplier supplier = validateSupplier("UPDATE", detailsDTO.getSupplierId());

        for (OrderDetailsRequestDTO.ArticleItem item : detailsDTO.getArticles()) {
            validateArticleAndLocation("UPDATE", item);
        }

        Order updatedOrder = orderMapper.fromOrderDetails(detailsDTO, supplier);
        updatedOrder.setId(existingOrder.getId());
        logEvent("UPDATE", "Order", "SUCCESS");
        return orderRepository.save(updatedOrder);
    }

    @Transactional
    public void delete(Long id) {
        validateOrderExists("DELETE", id);
        logEvent("DELETE", "Order", "SUCCESS");
        orderRepository.deleteById(id);
    }

    @Transactional
    public Order patch(Long id, Order orderUpdates) {
        Order existingOrder = validateOrderExists("PATCH", id);
         // TODO: Add more
        if (orderUpdates.getStatus() != null) {
            existingOrder.setStatus(orderUpdates.getStatus());
        }
        logEvent("PATCH", "Order", "SUCCESS");
        return orderRepository.save(existingOrder);
    }

    private Order validateOrderExists(String actionType, Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    logEvent(actionType, "Order", "NOT_FOUND");
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Order not found with id: " + id
                    );
                });
    }

    private Supplier validateSupplier(String actionType, Long supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> {
                    logEvent(actionType, "Supplier", "NOT_FOUND");
                    return new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Supplier not found with ID: " + supplierId
                    );
                });
    }

    private void validateArticleAndLocation(String actionType, OrderDetailsRequestDTO.ArticleItem item) {
        try {
            Long articleId = item.getArticleId();
            Long locationId = item.getLocationId();

            ArticleResponseDTO articleResponse = inventoryClient.fetchArticle(articleId);

            if (articleResponse == null) {
                logEvent(actionType, "Article", "NOT_FOUND");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Article not found in inventory-service with ID: " + articleId);
            }

            Double inventoryPrice = articleResponse.getPrice();
            Double requestPrice = item.getPrice();

            if (!inventoryPrice.equals(requestPrice)) {
                logEvent(actionType, "Article", "PRICE_MISMATCH");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("Price mismatch for article ID: %d. Expected: %.2f, Provided: %.2f",
                                articleId, inventoryPrice, requestPrice));
            }

            LocationResponseDTO locationResponse = inventoryClient.fetchLocation(locationId);

            if (locationResponse == null) {
                logEvent(actionType, "Location", "NOT_FOUND");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Location not found in inventory-service with ID: " + locationId);
            }

            Boolean inventoryIsCapacityFull = locationResponse.getIsCapacityFull();

            if (inventoryIsCapacityFull) {
                logEvent(actionType, "Location", "CAPACITY_FULL");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("Location capacity for article ID: %d is full", articleId));
            }
        } catch (ResponseStatusException e) {
            logEvent(actionType, "OrderItem", "FAILURE: " + e.getReason());
            throw e;
        }
    }

    private void logEvent(String actionType, String resourceName, String responseType) {
        try {
            systemEventsClient.logEvent(
                    Instant.now().toString(),
                    "order-service",
                    "current-username",
                    actionType,
                    resourceName,
                    responseType
            );
        } catch (Exception e) {
            log.error("Failed to log system event: " + e.getMessage());
        }
    }
}
