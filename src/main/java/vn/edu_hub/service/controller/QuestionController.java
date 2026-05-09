package vn.edu_hub.service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu_hub.service.dto.request.SaveQuestionsRequestDTO;
import vn.edu_hub.service.dto.response.CommonResponseDTO;
import vn.edu_hub.service.service.QuestionService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionController {
    QuestionService questionService;

    @PostMapping("/tasks/{taskId}/questions")
    public ResponseEntity<@NonNull CommonResponseDTO> saveQuestions(
            @PathVariable(name = "taskId") Long taskId,
            @Valid @RequestBody SaveQuestionsRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(questionService.saveQuestions(taskId, requestDTO));
    }
}
