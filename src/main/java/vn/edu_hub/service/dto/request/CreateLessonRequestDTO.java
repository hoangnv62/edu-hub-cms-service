package vn.edu_hub.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateLessonRequestDTO(
        @NotBlank(message = "Tiêu đề không được để trống")
        String title,

        String description,

        @NotBlank(message = "Nội dung không được để trống")
        String content, //html

        List<String> imageUrl,

        String videoUrl
) {
}
