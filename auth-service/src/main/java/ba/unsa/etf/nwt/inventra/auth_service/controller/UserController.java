package ba.unsa.etf.nwt.inventra.auth_service.controller;

import ba.unsa.etf.nwt.inventra.auth_service.dto.*;
import ba.unsa.etf.nwt.inventra.auth_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints for user registration and login")
public class UserController {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initRoles() {
        userService.initDefaultRoles();
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegisterRequestDTO request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Validates email and password")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO request) {
        return userService.login(request);
    }
}
