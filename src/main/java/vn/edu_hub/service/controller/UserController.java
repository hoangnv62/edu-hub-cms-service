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
import vn.edu_hub.service.dto.request.CreateUserRequestDTO;
import vn.edu_hub.service.dto.response.UserResponseDTO;
import vn.edu_hub.service.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ResponseEntity<@NonNull UserResponseDTO> create(@Valid @RequestBody CreateUserRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.createUser(requestDTO));
    }
}
