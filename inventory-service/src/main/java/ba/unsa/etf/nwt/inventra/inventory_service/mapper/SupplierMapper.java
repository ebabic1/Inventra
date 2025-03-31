package ba.unsa.etf.nwt.inventra.inventory_service.mapper;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.SupplierDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    SupplierDTO toDTO(Supplier supplier);
    Supplier toEntity(SupplierDTO warehouseDTO);
}
