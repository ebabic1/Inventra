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
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints for user management")
public class UserController {

    public final UserService userService;

    public final JwtUtil jwtUtil;

    public final TokenBlacklistService tokenBlacklistService;

    public UserController(UserService userService, JwtUtil jwtUtil, TokenBlacklistService blacklistService) {
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistService = blacklistService;
        this.userService = userService;
    }

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

    @GetMapping("/me")
    @Operation(summary = "Get current logged-in user info", description = "Returns info about the currently authenticated user")
    public ResponseEntity<UserDetailsResponseDTO> getCurrentUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        Claims claims = jwtUtil.extractAllClaims(token);

        String userEmail = claims.getSubject();

        if (userEmail == null || userEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetailsResponseDTO userDetails = userService.getUserByEmail(userEmail);
        if (userDetails == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userDetails);
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

    @GetMapping("/emails")
    @Operation(summary = "Get all user emails", description = "Returns a list of email addresses of all registered users.")
    public ResponseEntity<List<String>> getAllUserEmails(@RequestHeader HttpHeaders headers) {
        List<String> emails = userService.getAllEmails();
        return ResponseEntity.ok(emails);
    }

}

