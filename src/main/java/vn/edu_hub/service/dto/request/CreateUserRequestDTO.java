package vn.edu_hub.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import vn.edu_hub.service.dto.validation.Role;

public record CreateUserRequestDTO(
        @NotBlank(message = "Tên đăng nhập không được để trống")
        String username,

        @NotBlank(message = "Mật khẩu không được để trống")
        String password,

        @NotBlank(message = "Tên người dùng không được để trống")
        String fullName,

        @NotBlank(message = "Role không được để trống")
        @Role
        String role
) {
}
