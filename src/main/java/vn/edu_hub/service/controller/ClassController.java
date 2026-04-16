package vn.edu_hub.service.controller;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu_hub.service.dto.request.AddSubjectRequestDTO;
import vn.edu_hub.service.dto.request.ClassRequestDTO;
import vn.edu_hub.service.dto.response.ClassResponseDTO;
import vn.edu_hub.service.dto.response.CommonResponseDTO;
import vn.edu_hub.service.security.CurrentUserId;
import vn.edu_hub.service.service.ClassService;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassController {
    ClassService classService;

    @PostMapping
    public ResponseEntity<@NonNull CommonResponseDTO> create(
            @Valid @NonNull @RequestBody ClassRequestDTO request,
            @CurrentUserId Long currentUserId
    ) {
        return ResponseEntity.ok(classService.create(request, currentUserId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull CommonResponseDTO> update(
            @PathVariable Long id,
            @Valid @NonNull @RequestBody ClassRequestDTO request,
            @CurrentUserId Long currentUserId) {
        return ResponseEntity.ok(classService.update(id, request, currentUserId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull CommonResponseDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok(classService.delete(id));
    }

    @GetMapping
    public ResponseEntity<@NonNull Page<@NonNull ClassResponseDTO>> getAll(
            @CurrentUserId Long currentUserId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "gradeLevel", required = false) String gradeLevel,
            Pageable pageable
    ) {
        return ResponseEntity.ok(classService.getAll(currentUserId, keyword, gradeLevel, pageable));
    }

    @PostMapping("/{id}/add-subject")
    public ResponseEntity<@NonNull CommonResponseDTO> addSubject(
            @PathVariable Long id, @RequestBody @Valid AddSubjectRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(classService.addSubject(id, requestDTO.subjectId()));
    }
}
