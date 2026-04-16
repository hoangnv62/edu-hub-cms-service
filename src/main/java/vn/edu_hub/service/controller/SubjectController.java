package vn.edu_hub.service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu_hub.service.dto.request.SubjectRequestDTO;
import vn.edu_hub.service.dto.response.CommonResponseDTO;
import vn.edu_hub.service.dto.response.SubjectResponseDTO;
import vn.edu_hub.service.service.SubjectService;

@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectController {
    SubjectService subjectService;

    @GetMapping
    public ResponseEntity<@NonNull Page<@NonNull SubjectResponseDTO>> search(
            @RequestParam(name = "name", required = false) String name, @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(subjectService.findAll(name, pageable));
    }

    @PostMapping
    public ResponseEntity<@NonNull CommonResponseDTO> create(@RequestBody @Valid @NonNull SubjectRequestDTO request) {
        return ResponseEntity.ok(subjectService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull CommonResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid @NonNull SubjectRequestDTO request
    ) {
        return ResponseEntity.ok(subjectService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull Void> delete(
            @PathVariable Long id
    ){
        subjectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

