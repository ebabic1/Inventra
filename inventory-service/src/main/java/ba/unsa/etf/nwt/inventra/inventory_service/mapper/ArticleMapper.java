package ba.unsa.etf.nwt.inventra.inventory_service.mapper;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.ArticleDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Article;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(target = "location", source = "locationId", qualifiedByName = "idToLocation")
    Article toEntity(ArticleDTO articleDTO);

    @Mapping(target = "locationId", source = "location.id")
    ArticleDTO toDTO(Article article);

    @Named("idToLocation")
    default Location idToLocation(Long id) {
        if (id == null) return null;
        Location location = new Location();
        location.setId(id);
        return location;
    }
}