package ba.unsa.etf.nwt.inventra.order_service.service;

import ba.unsa.etf.nwt.inventra.order_service.client.InventoryClient;
import ba.unsa.etf.nwt.inventra.order_service.dto.ArticleResponseDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.LocationResponseDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDetailsRequestDTO;
import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDetailsResponseDTO;
import ba.unsa.etf.nwt.inventra.order_service.mapper.OrderMapper;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.model.Supplier;
import ba.unsa.etf.nwt.inventra.order_service.repository.OrderRepository;
import ba.unsa.etf.nwt.inventra.order_service.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final OrderMapper orderMapper;

    @Autowired
    private RestTemplate restTemplate;

    private final InventoryClient inventoryClient;

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public OrderDetailsResponseDTO findById(Long id) {
        Order order = validateOrderExists(id);
        return orderMapper.toDetailsResponseDTO(order);
    }

    @Transactional
    public List<Order> createBatch(List<OrderDetailsRequestDTO> detailsDTOs) {
        List<Order> orders = detailsDTOs.stream().map(requestDTO -> {
            Supplier supplier = validateSupplier(requestDTO.getSupplierId());

            for (OrderDetailsRequestDTO.ArticleItem item : requestDTO.getArticles()) {
                validateArticleAndLocation(item);
            }

            return orderMapper.fromOrderDetails(requestDTO, supplier);
        }).toList();

        return orderRepository.saveAll(orders);
    }

    @Transactional
    public Order create(OrderDetailsRequestDTO detailsDTO) {
        Supplier supplier = validateSupplier(detailsDTO.getSupplierId());

        for (OrderDetailsRequestDTO.ArticleItem item : detailsDTO.getArticles()) {
            validateArticleAndLocation(item);
        }

        Order order = orderMapper.fromOrderDetails(detailsDTO, supplier);

        return orderRepository.save(order);
    }

    @Transactional
    public Order update(Long id, OrderDetailsRequestDTO detailsDTO) {
        Order existingOrder = validateOrderExists(id);

        Supplier supplier = validateSupplier(detailsDTO.getSupplierId());

        for (OrderDetailsRequestDTO.ArticleItem item : detailsDTO.getArticles()) {
            validateArticleAndLocation(item);
        }

        Order updatedOrder = orderMapper.fromOrderDetails(detailsDTO, supplier);
        updatedOrder.setId(existingOrder.getId());

        return orderRepository.save(updatedOrder);
    }

    @Transactional
    public void delete(Long id) {
        validateOrderExists(id);
        orderRepository.deleteById(id);
    }

    @Transactional
    public Order patch(Long id, Order orderUpdates) {
        Order existingOrder = validateOrderExists(id);
         // TODO: Add more
        if (orderUpdates.getStatus() != null) {
            existingOrder.setStatus(orderUpdates.getStatus());
        }
        return orderRepository.save(existingOrder);
    }

    private Order validateOrderExists(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + id));
    }

    private Supplier validateSupplier(Long supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Supplier not found with ID: " + supplierId
                ));
    }

    private void validateArticleAndLocation(OrderDetailsRequestDTO.ArticleItem item) {
        Long articleId = item.getArticleId();
        Long locationId = item.getLocationId();

        ArticleResponseDTO articleResponse = inventoryClient.fetchArticle(articleId);

        if (articleResponse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Article not found in inventory-service with ID: " + articleId);
        }

        Double inventoryPrice = articleResponse.getPrice();
        Double requestPrice = item.getPrice();

        if (!inventoryPrice.equals(requestPrice)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Price mismatch for article ID: %d. Expected: %.2f, Provided: %.2f",
                            articleId, inventoryPrice, requestPrice));
        }

        LocationResponseDTO locationResponse = inventoryClient.fetchLocation(locationId);

        if (locationResponse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Location not found in inventory-service with ID: " + locationId);
        }

        Boolean inventoryIsCapacityFull = locationResponse.getIsCapacityFull();

        if (inventoryIsCapacityFull) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Location capacity for article ID: %d is full", articleId));
        }
    }
}

