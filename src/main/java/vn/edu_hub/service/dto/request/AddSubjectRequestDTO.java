package vn.edu_hub.service.dto.request;

import jakarta.validation.constraints.NotNull;

public record AddSubjectRequestDTO(
        @NotNull(message = "Mã môn học không được để trống")
        Long subjectId
) {
}
