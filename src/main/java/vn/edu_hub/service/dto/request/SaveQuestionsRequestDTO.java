package vn.edu_hub.service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaveQuestionsRequestDTO {
    @Valid
    @NotEmpty(message = "Danh sách câu hỏi không được để trống")
    List<QuestionRequestDTO> questions;
}
