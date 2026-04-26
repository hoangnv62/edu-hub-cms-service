package vn.edu_hub.service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu_hub.service.dto.request.LoginRequestDTO;
import vn.edu_hub.service.dto.response.AuthResponseDTO;
import vn.edu_hub.service.dto.response.UserResponseDTO;
import vn.edu_hub.service.security.CurrentUserId;
import vn.edu_hub.service.service.AuthService;
import vn.edu_hub.service.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authenticate")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    UserService userService;
    @PostMapping
    public ResponseEntity<@NonNull AuthResponseDTO> authenticate(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.authenticate(loginRequestDTO));
    }

    @GetMapping("/me")
    public ResponseEntity<@NonNull UserResponseDTO> getCurrentUser(@CurrentUserId Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
