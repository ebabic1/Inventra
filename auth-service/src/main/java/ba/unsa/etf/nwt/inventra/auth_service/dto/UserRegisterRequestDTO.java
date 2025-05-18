package ba.unsa.etf.nwt.inventra.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequestDTO {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 20)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 20)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Size(max = 100)
    private String bio;
}
