package vn.edu_hub.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import vn.edu_hub.service.dto.validation.GradeLevel;

public record ClassRequestDTO(
        @NotBlank(message = "Tên không được để trống")
        String name,
        @NotBlank(message = "Khối không được để trống")
        @GradeLevel
        String gradeLevel,
        String description
) {
}
