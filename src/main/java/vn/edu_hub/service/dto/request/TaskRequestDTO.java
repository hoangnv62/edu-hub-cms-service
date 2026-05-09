package vn.edu_hub.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import vn.edu_hub.service.dto.validation.TaskType;

public record TaskRequestDTO(
        @NotBlank(message = "Tên bài tập không được để trống")
        String name,
        String description,

        @NotBlank(message= "Loại bài tập không được để trống")
        @TaskType(message = "Loại bài tập không hợp lệ")
        String type
) {

}
