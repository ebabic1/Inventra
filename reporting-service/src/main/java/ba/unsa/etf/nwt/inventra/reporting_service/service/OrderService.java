package ba.unsa.etf.nwt.inventra.reporting_service.service;

import ba.unsa.etf.nwt.inventra.reporting_service.model.Order;
import ba.unsa.etf.nwt.inventra.reporting_service.model.OrderArticle;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.OrderReportRepository;
import ba.unsa.etf.nwt.inventra.reporting_service.repository.OrderRepository;
import ba.unsa.etf.nwt.system_events_service.ActionType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderReportRepository orderReportRepository;

    @Transactional
    public Order create(Order order) {
        return orderRepository.saveAndFlush(order);
    }

    @Transactional
    public void update(Long id, Order order) {
        Order existingOrder = validateOrderExists(ActionType.UPDATE, id);
        existingOrder.setId(order.getId());
        orderRepository.save(existingOrder);
    }

    @Transactional
    public void delete(Long id) {
        Order order = validateOrderExists(ActionType.DELETE, id);
        List<Long> orderArticleIds = order.getOrderArticles().stream()
                .map(OrderArticle::getId)
                .toList();

        orderReportRepository.deleteByOrderArticleIdIn(orderArticleIds);
        orderRepository.deleteById(id);
    }

    private Order validateOrderExists(ActionType actionType, Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + id));
    }
}
