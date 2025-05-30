package ba.unsa.etf.nwt.inventra.order_service.service;

import ba.unsa.etf.nwt.inventra.order_service.client.InventoryClient;
import ba.unsa.etf.nwt.inventra.order_service.dto.*;
import ba.unsa.etf.nwt.inventra.order_service.mapper.OrderMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.model.Supplier;
import ba.unsa.etf.nwt.inventra.order_service.repository.OrderRepository;
import ba.unsa.etf.nwt.inventra.order_service.repository.SupplierRepository;
import ba.unsa.etf.nwt.system_events_service.ActionType;
import ba.unsa.etf.nwt.system_events_service.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
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
        logEvent(ActionType.GET_ALL, "Order", ResponseType.SUCCESS);
        return page;
    }

    public OrderDetailsResponseDTO findById(Long id) {
        Order order = validateOrderExists(ActionType.GET, id);
        logEvent(ActionType.GET, "Order", ResponseType.SUCCESS);
        return orderMapper.toDetailsResponseDTO(order);
    }

    @Transactional
    public List<Order> createBatch(List<OrderDetailsRequestDTO> detailsDTOs) {
        List<Order> orders = detailsDTOs.stream().map(requestDTO -> {
            Supplier supplier = validateSupplier(ActionType.CREATE_BATCH, requestDTO.getSupplierId());

            for (OrderDetailsRequestDTO.ArticleItem item : requestDTO.getArticles()) {
                validateArticleAndLocation(ActionType.CREATE_BATCH, item);
            }

            return orderMapper.fromOrderDetails(requestDTO, supplier);
        }).toList();
        logEvent(ActionType.CREATE_BATCH, "Order", ResponseType.SUCCESS);
        return orderRepository.saveAll(orders);
    }

    @Transactional
    public Order create(OrderDetailsRequestDTO detailsDTO) {
        Supplier supplier = validateSupplier(ActionType.CREATE, detailsDTO.getSupplierId());

        for (OrderDetailsRequestDTO.ArticleItem item : detailsDTO.getArticles()) {
            validateArticleAndLocation(ActionType.CREATE, item);
        }

        Order order = orderMapper.fromOrderDetails(detailsDTO, supplier);
        logEvent(ActionType.CREATE, "Order", ResponseType.SUCCESS);
        return orderRepository.save(order);
    }

    @Transactional
    public Order update(Long id, OrderDetailsRequestDTO detailsDTO) {
        Order existingOrder = validateOrderExists(ActionType.UPDATE, id);
        Supplier supplier = validateSupplier(ActionType.UPDATE, detailsDTO.getSupplierId());

        for (OrderDetailsRequestDTO.ArticleItem item : detailsDTO.getArticles()) {
            validateArticleAndLocation(ActionType.UPDATE, item);
        }

        Order updatedOrder = orderMapper.fromOrderDetails(detailsDTO, supplier);
        updatedOrder.setId(existingOrder.getId());

        logEvent(ActionType.UPDATE, "Order", ResponseType.SUCCESS);
        return orderRepository.save(updatedOrder);
    }

    @Transactional
    public void delete(Long id) {
        validateOrderExists(ActionType.DELETE, id);
        orderRepository.deleteById(id);
        logEvent(ActionType.DELETE, "Order", ResponseType.SUCCESS);
    }

    @Transactional
    public Order patch(Long id, Order orderUpdates) {
        Order existingOrder = validateOrderExists(ActionType.PATCH, id);

        if (orderUpdates.getStatus() != null) {
            existingOrder.setStatus(orderUpdates.getStatus());
        }

        logEvent(ActionType.PATCH, "Order", ResponseType.SUCCESS);
        return orderRepository.save(existingOrder);
    }

    private Order validateOrderExists(ActionType actionType, Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    logEvent(actionType, "Order", ResponseType.FAILURE);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + id);
                });
    }

    private Supplier validateSupplier(ActionType actionType, Long supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> {
                    logEvent(actionType, "Supplier", ResponseType.FAILURE);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier not found with ID: " + supplierId);
                });
    }

    private void validateArticleAndLocation(ActionType actionType, OrderDetailsRequestDTO.ArticleItem item) {
        try {
            Long articleId = item.getArticleId();
            Long locationId = item.getLocationId();

            ArticleResponseDTO articleResponse = inventoryClient.fetchArticle(articleId);
            if (articleResponse == null) {
                logEvent(actionType, "Article", ResponseType.FAILURE);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found with ID: " + articleId);
            }

            Double inventoryPrice = articleResponse.getPrice();
            if (!inventoryPrice.equals(item.getPrice())) {
                logEvent(actionType, "Article", ResponseType.FAILURE);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(
                        "Price mismatch for article ID: %d. Expected: %.2f, Provided: %.2f",
                        articleId, inventoryPrice, item.getPrice()));
            }

            LocationResponseDTO locationResponse = inventoryClient.fetchLocation(locationId);
            if (locationResponse == null) {
                logEvent(actionType, "Location", ResponseType.FAILURE);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found with ID: " + locationId);
            }

            if (Boolean.TRUE.equals(locationResponse.getIsCapacityFull())) {
                logEvent(actionType, "Location", ResponseType.FAILURE);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is full for article ID: " + articleId);
            }

        } catch (ResponseStatusException e) {
            logEvent(actionType, "OrderItem", ResponseType.FAILURE);
            throw e;
        }
    }

    private void logEvent(ActionType actionType, String resourceName, ResponseType responseType) {
        try {
            systemEventsClient.logEvent(
                    Instant.now().toString(),
                    "order-service",
                    "current-user",
                    actionType,
                    resourceName,
                    responseType
            );
        } catch (Exception e) {
            log.error("Failed to log system event: {}", e.getMessage());
        }
    }
}
