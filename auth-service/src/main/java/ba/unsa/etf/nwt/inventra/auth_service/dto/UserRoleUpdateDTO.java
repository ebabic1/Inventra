package ba.unsa.etf.nwt.inventra.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleUpdateDTO {

    @NotBlank(message = "Role name is required")
    private String roleName;
}
