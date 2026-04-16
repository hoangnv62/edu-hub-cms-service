package vn.edu_hub.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequestDTO {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    String password;
    boolean isRememberMe = false;
}
