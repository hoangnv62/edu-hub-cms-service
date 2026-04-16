package vn.edu_hub.service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu_hub.service.dto.request.LoginRequestDTO;
import vn.edu_hub.service.dto.response.AuthResponseDTO;
import vn.edu_hub.service.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authenticate")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping
    public ResponseEntity<@NonNull AuthResponseDTO> authenticate(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.authenticate(loginRequestDTO));
    }
}
