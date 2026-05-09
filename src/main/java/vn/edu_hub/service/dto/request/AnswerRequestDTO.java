package vn.edu_hub.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AnswerRequestDTO (
        Long id,
        @NotBlank(message = "Nhãn câu hỏi không được để trống")
        String label,
        @NotBlank(message = "Nội dung câu hỏi không được để trống")
        String content,
        boolean isCorrect
){

}
