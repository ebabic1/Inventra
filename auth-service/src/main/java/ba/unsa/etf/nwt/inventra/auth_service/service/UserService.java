package ba.unsa.etf.nwt.inventra.auth_service.service;

import ba.unsa.etf.nwt.inventra.auth_service.dto.*;
import ba.unsa.etf.nwt.inventra.auth_service.mapper.UserMapper;
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
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }

    public void initDefaultRoles() {
        createRoleIfNotExists("USER");
        createRoleIfNotExists("WAREHOUSE_OPERATOR");
        createRoleIfNotExists("WAREHOUSE_MANAGER");
        createRoleIfNotExists("ADMIN");
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            roleRepository.save(new Role(null, roleName));
        }
    }

    public ResponseEntity<UserRegisterResponseDTO> register(UserRegisterRequestDTO dto) {
        Optional<User> existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new UserRegisterResponseDTO("Email already in use."));
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBio(dto.getBio());

        Role role = roleRepository.findByName("USER").orElseThrow();
        user.setRole(role);

        userRepository.save(user);
        return ResponseEntity.ok(new UserRegisterResponseDTO("User registered successfully."));
    }

    public ResponseEntity<UserLoginResponseDTO> login(UserLoginRequestDTO dto) {
        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(dto.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserLoginResponseDTO("Invalid credentials.", null));
        }

        User user = userOpt.get();
        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new UserLoginResponseDTO("Login successful.", token));
    }

    public List<UserDetailsResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
    }

    public UserDetailsResponseDTO getUserById(UUID uuid) {
        User user = userRepository.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }

    public UserDetailsResponseDTO updateUserDetails(UUID uuid, UserUpdateDTO dto) {
        User user = userRepository.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getBio() != null) user.setBio(dto.getBio());

        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public UserDetailsResponseDTO updateUserRole(UUID uuid, UserRoleUpdateDTO dto) {
        User user = userRepository.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(dto.getRoleName()).orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(role);
        userRepository.save(user);

        return userMapper.toDTO(user);
    }

    public void deleteUser(UUID uuid) {
        if (!userRepository.existsById(uuid)) throw new RuntimeException("User not found");
        userRepository.deleteById(uuid);
    }

    public UserDetailsResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return userMapper.toDTO(user);
    }

    public List<String> getAllEmails() {
        return userRepository.findAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }
}
