package ba.unsa.etf.nwt.inventra.auth_service.mapper;

import ba.unsa.etf.nwt.inventra.auth_service.dto.UserDetailsResponseDTO;
import ba.unsa.etf.nwt.inventra.auth_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", source = "role.name")
    UserDetailsResponseDTO toDTO(User user);
}