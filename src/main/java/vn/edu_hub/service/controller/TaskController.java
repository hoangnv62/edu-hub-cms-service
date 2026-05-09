package vn.edu_hub.service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu_hub.service.dto.request.TaskClassRequestDTO;
import vn.edu_hub.service.dto.request.TaskRequestDTO;
import vn.edu_hub.service.dto.response.CommonResponseDTO;
import vn.edu_hub.service.dto.response.TaskResponseDTO;
import vn.edu_hub.service.security.CurrentUserId;
import vn.edu_hub.service.service.TaskService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {
    TaskService taskService;

    @GetMapping
    public ResponseEntity<@NonNull Page<@NonNull TaskResponseDTO>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @CurrentUserId Long currentUserId,
            Pageable pageable) {
        return ResponseEntity.ok(taskService.searchByCriterial(keyword, dateFrom, dateTo, currentUserId, pageable));
    }

    @PostMapping
    public ResponseEntity<@NonNull CommonResponseDTO> create(
            @Valid @NonNull @RequestBody TaskRequestDTO request,
            @CurrentUserId Long currentUserId) {
        return ResponseEntity.ok(taskService.create(request, currentUserId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull CommonResponseDTO> update(
            @PathVariable Long id,
            @Valid @NonNull @RequestBody TaskRequestDTO request,
            @CurrentUserId Long currentUserId) {
        return ResponseEntity.ok(taskService.update(id, request, currentUserId));
    }

    @DeleteMapping
    public ResponseEntity<@NonNull Void> deleteByIds(
            @RequestParam List<Long> ids) {
        taskService.deleteByIds(ids);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/classes")
    public ResponseEntity<@NonNull CommonResponseDTO> assignForClass(
            @PathVariable Long taskId,
            @Valid @NonNull @RequestBody TaskClassRequestDTO request) {
        return ResponseEntity.ok(taskService.assignForClass(taskId, request));
    }

    @DeleteMapping("/{taskId}/classes")
    public ResponseEntity<@NonNull Void> unassignForClass(
            @PathVariable Long taskId,
            @RequestParam List<Long> classIds) {
        taskService.unassignForClass(taskId, classIds);
        return ResponseEntity.noContent().build();
    }
}
