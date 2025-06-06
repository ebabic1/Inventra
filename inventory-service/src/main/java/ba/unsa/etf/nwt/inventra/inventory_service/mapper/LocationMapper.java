package ba.unsa.etf.nwt.inventra.inventory_service.mapper;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.LocationDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Location;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    @Mapping(source = "warehouse.id", target = "warehouseId")  // Manually map warehouseId
    LocationDTO toDTO(Location location);

    @Mapping(source = "warehouseId", target = "warehouse")  // Manually map warehouseId to Warehouse
    Location toEntity(LocationDTO locationDTO);

    default Warehouse map(Long warehouseId) {
        if (warehouseId == null) {
            return null;
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setId(warehouseId);
        return warehouse;
    }
}
