package ba.unsa.etf.nwt.inventra.auth_service.service;

import ba.unsa.etf.nwt.inventra.auth_service.dto.*;
import ba.unsa.etf.nwt.inventra.auth_service.model.*;
import ba.unsa.etf.nwt.inventra.auth_service.repository.*;
import ba.unsa.etf.nwt.inventra.auth_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        if (roleRepo.findByName("USER").isEmpty()) {
            roleRepo.save(new Role(null, "USER"));
        }
    }

    public ResponseEntity<UserResponseDTO> register(UserRegisterRequestDTO dto) {
        Optional<User> existingUser = userRepo.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new UserResponseDTO("Email already in use.", null));
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
        return ResponseEntity.ok(new UserResponseDTO("User registered successfully.", null));
    }

    public ResponseEntity<UserResponseDTO> login(UserLoginRequestDTO dto) {
        Optional<User> userOpt = userRepo.findByEmail(dto.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(dto.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserResponseDTO("Invalid credentials.", null));
        }

        User user = userOpt.get();
        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new UserResponseDTO("Login successful.", token));
    }
}
