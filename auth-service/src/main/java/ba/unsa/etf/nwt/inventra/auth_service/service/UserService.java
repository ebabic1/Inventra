package ba.unsa.etf.nwt.inventra.auth_service.service;

import ba.unsa.etf.nwt.inventra.auth_service.dto.*;
import ba.unsa.etf.nwt.inventra.auth_service.model.*;
import ba.unsa.etf.nwt.inventra.auth_service.repository.*;
import ba.unsa.etf.nwt.inventra.auth_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void initDefaultRoles() {
        createRoleIfNotExists("USER");
        createRoleIfNotExists("WAREHOUSE_OPERATOR");
        createRoleIfNotExists("WAREHOUSE_MANAGER");
        createRoleIfNotExists("ADMIN");
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepo.findByName(roleName).isEmpty()) {
            roleRepo.save(new Role(null, roleName));
        }
    }

    public ResponseEntity<UserRegisterResponseDTO> register(UserRegisterRequestDTO dto) {
        Optional<User> existingUser = userRepo.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new UserRegisterResponseDTO("Email already in use."));
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBio(dto.getBio());

        Role role = roleRepo.findByName("USER").orElseThrow();
        user.setRole(role);

        userRepo.save(user);
        return ResponseEntity.ok(new UserRegisterResponseDTO("User registered successfully."));
    }

    public ResponseEntity<UserLoginResponseDTO> login(UserLoginRequestDTO dto) {
        Optional<User> userOpt = userRepo.findByEmail(dto.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(dto.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserLoginResponseDTO("Invalid credentials.", null));
        }

        User user = userOpt.get();
        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new UserLoginResponseDTO("Login successful.", token));
    }

    public List<UserDetailsResponseDTO> getAllUsers() {
        return userRepo.findAll().stream().map(this::toDTO).toList();
    }

    public UserDetailsResponseDTO getUserById(UUID uuid) {
        User user = userRepo.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));
        return toDTO(user);
    }

    public UserDetailsResponseDTO updateUserDetails(UUID uuid, UserUpdateDTO dto) {
        User user = userRepo.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getBio() != null) user.setBio(dto.getBio());

        userRepo.save(user);
        return toDTO(user);
    }

    public UserDetailsResponseDTO updateUserRole(UUID uuid, UserRoleUpdateDTO dto) {
        User user = userRepo.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepo.findByName(dto.getRoleName()).orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(role);
        userRepo.save(user);

        return toDTO(user);
    }

    public void deleteUser(UUID uuid) {
        if (!userRepo.existsById(uuid)) throw new RuntimeException("User not found");
        userRepo.deleteById(uuid);
    }

    private UserDetailsResponseDTO toDTO(User user) {
        UserDetailsResponseDTO dto = new UserDetailsResponseDTO();
        dto.setUuid(user.getUuid());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setRole(user.getRole().getName());
        return dto;
    }

}
