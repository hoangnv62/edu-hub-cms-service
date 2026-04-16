package vn.edu_hub.service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu_hub.service.constants.ApiResponseCode;
import vn.edu_hub.service.constants.Authorities;
import vn.edu_hub.service.domain.User;
import vn.edu_hub.service.dto.request.LoginRequestDTO;
import vn.edu_hub.service.dto.response.AuthResponseDTO;
import vn.edu_hub.service.dto.response.UserResponseDTO;
import vn.edu_hub.service.exception.BusinessException;
import vn.edu_hub.service.repository.UserRepository;
import vn.edu_hub.service.security.jwt.TokenProvider;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    TokenProvider tokenProvider;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public AuthResponseDTO authenticate(LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Không tìm thấy tài khoản"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Mật khẩu không đúng");
        }
        String accessToken = tokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole(), request.isRememberMe());
        long tokenValidityInMilliseconds = request.isRememberMe() ?
                tokenProvider.getTokenValidityInMillisecondsForRememberMe() :
                tokenProvider.getTokenValidityInMilliseconds();
        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .created(Instant.now().getEpochSecond())
                .expiresIn((int) tokenValidityInMilliseconds / 1000)
                .user(convertToDTO(user))
                .build();
    }

    private UserResponseDTO convertToDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(Authorities.find(user.getRole()).getName())
                .build();
    }
}
