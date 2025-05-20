package ba.unsa.etf.nwt.inventra.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponseDTO {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private String role;
}
