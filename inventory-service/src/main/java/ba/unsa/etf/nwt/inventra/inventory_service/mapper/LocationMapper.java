package ba.unsa.etf.nwt.inventra.inventory_service.mapper;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.LocationDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDTO toDTO(Location location);
    Location toEntity(LocationDTO locationDTO);
}
