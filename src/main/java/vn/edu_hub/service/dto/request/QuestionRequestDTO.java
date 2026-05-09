package vn.edu_hub.service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record QuestionRequestDTO (
        Long id,
        @Positive(message = "Vị trí câu hỏi phải lớn hơn 0")
        Integer position,
        @NotBlank(message = "Nội dung câu hỏi không được để trống")
        String content,
        @NotBlank(message = "Điểm câu hỏi không được để trống")
        Float score,

        @Valid
        @NotEmpty(message = "Danh sách câu trả lời không được trống")
        List<AnswerRequestDTO>answers
) {
}
