package vn.edu_hub.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vn.edu_hub.service.dto.validation.MyDateTime;

import java.util.List;

public record TaskClassRequestDTO(
        @NotNull(message = "")
        List<Long> classIds,

        @NotBlank(message = "Hạn nộp bài không được để trống")
        @MyDateTime(message = "Hạn nộp bài không hợp lệ")
        String dueDate
) {
}
