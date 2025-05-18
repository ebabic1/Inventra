package ba.unsa.etf.nwt.inventra.auth_service.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @NotNull(message = "First name is required")
    @Size(min = 2, max = 20, message = "First name should be between 2 and 20 characters")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Size(min = 2, max = 20, message = "Last name should be between 2 and 20 characters")
    private String lastName;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Size(max = 100, message = "Bio should be at most 100 characters")
    private String bio;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull(message = "Role is required")
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}