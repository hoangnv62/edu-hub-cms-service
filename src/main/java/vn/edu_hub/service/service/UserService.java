package vn.edu_hub.service.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu_hub.service.constants.ApiResponseCode;
import vn.edu_hub.service.constants.Authorities;
import vn.edu_hub.service.domain.User;
import vn.edu_hub.service.dto.request.CreateUserRequestDTO;
import vn.edu_hub.service.dto.response.UserResponseDTO;
import vn.edu_hub.service.exception.BusinessException;
import vn.edu_hub.service.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(CreateUserRequestDTO request) {
        if (userRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Tên đăng nhập đã tồn tại");
        }
        Authorities role = Authorities.find(request.role());
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .role(role.getId())
                .build();
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    private UserResponseDTO convertToDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(Authorities.find(user.getRole()).name())
                .build();
    }
}
