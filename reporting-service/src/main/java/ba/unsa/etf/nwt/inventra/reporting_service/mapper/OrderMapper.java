package ba.unsa.etf.nwt.inventra.reporting_service.mapper;

import ba.unsa.etf.nwt.inventra.reporting_service.dto.OrderDTO;
import ba.unsa.etf.nwt.inventra.reporting_service.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderDTO orderToOrderDTO(Order order);

    Order orderDTOToOrder(OrderDTO orderDTO);
}