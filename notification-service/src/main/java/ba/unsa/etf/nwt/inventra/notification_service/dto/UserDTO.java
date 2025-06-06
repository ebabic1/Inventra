package ba.unsa.etf.nwt.inventra.notification_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {

    // TODO: Do we need constraints?

    private UUID uuid;

    private String firstName;

    private String lastName;

    private String email;
}
