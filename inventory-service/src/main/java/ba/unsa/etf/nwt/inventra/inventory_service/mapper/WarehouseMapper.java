package ba.unsa.etf.nwt.inventra.inventory_service.mapper;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.WarehouseDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    WarehouseDTO toDTO(Warehouse warehouse);
    Warehouse toEntity(WarehouseDTO warehouseDTO);
}
