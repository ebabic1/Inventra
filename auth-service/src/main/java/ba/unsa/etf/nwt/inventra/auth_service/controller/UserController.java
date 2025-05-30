package ba.unsa.etf.nwt.inventra.auth_service.controller;

import ba.unsa.etf.nwt.inventra.auth_service.dto.*;
import ba.unsa.etf.nwt.inventra.auth_service.service.TokenBlacklistService;
import ba.unsa.etf.nwt.inventra.auth_service.service.UserService;
import ba.unsa.etf.nwt.inventra.auth_service.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints for user management")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;


    @PostConstruct
    public void initRoles() {
        userService.initDefaultRoles();
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    public ResponseEntity<UserRegisterResponseDTO> register(@Valid @RequestBody UserRegisterRequestDTO request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Validates email and password")
    public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO request) {
        return userService.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing token");
        }

        String token = authHeader.substring(7);
        Claims claims = jwtUtil.extractAllClaims(token);
        String jti = claims.getId();
        Date expiration = claims.getExpiration();

        long ttl = expiration.getTime() - System.currentTimeMillis();
        tokenBlacklistService.blacklistToken(jti, ttl);

        return ResponseEntity.ok(new MessageResponseDTO("Logged out successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public List<UserDetailsResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get user by ID")
    public UserDetailsResponseDTO getUserById(@PathVariable UUID uuid) {
        return userService.getUserById(uuid);
    }

    @PatchMapping("/{uuid}/details")
    @Operation(summary = "Update user details")
    public UserDetailsResponseDTO updateUserDetails(@PathVariable UUID uuid, @RequestBody UserUpdateDTO dto) {
        return userService.updateUserDetails(uuid, dto);
    }

    @PatchMapping("/{uuid}/role")
    @Operation(summary = "Update user role")
    public UserDetailsResponseDTO updateUserRole(@PathVariable UUID uuid, @RequestBody UserRoleUpdateDTO dto) {
        return userService.updateUserRole(uuid, dto);
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete user")
    public ResponseEntity<String> deleteUser(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
        return ResponseEntity.noContent().build();
    }
}
