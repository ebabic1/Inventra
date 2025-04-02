package ba.unsa.etf.nwt.inventra.order_service.mapper;

import ba.unsa.etf.nwt.inventra.order_service.dto.OrderDTO;
import ba.unsa.etf.nwt.inventra.order_service.model.Order;
import ba.unsa.etf.nwt.inventra.order_service.model.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "supplier", source = "supplierId", qualifiedByName = "idToSupplier")
    Order toEntity(OrderDTO dto);

    @Mapping(target = "supplierId", source = "supplier.id")
    OrderDTO toDTO(Order order);

    @Named("idToSupplier")
    default Supplier idToSupplier(Long id) {
        if (id == null) return null;
        Supplier supplier = new Supplier();
        supplier.setId(id);
        return supplier;
    }
}
