package vn.edu_hub.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SubjectRequestDTO(
        @NotBlank(message = "Tên môn học không được để trống")
        String name
) {
}
