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
import vn.edu_hub.service.dto.request.AssignTaskRequestDTO;
import vn.edu_hub.service.dto.request.ClassRequestDTO;
import vn.edu_hub.service.dto.response.*;
import vn.edu_hub.service.dto.validation.MyDate;
import vn.edu_hub.service.dto.validation.TaskType;
import vn.edu_hub.service.security.CurrentUserId;
import vn.edu_hub.service.service.ClassService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassController {
    ClassService classService;

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull ClassDetailResponseDTO> getClass(@PathVariable Long id) {
        return ResponseEntity.ok(classService.getDetail(id));
    }

    @PostMapping
    public ResponseEntity<@NonNull CommonResponseDTO> create(
            @Valid @NonNull @RequestBody ClassRequestDTO request,
            @CurrentUserId Long currentUserId) {
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
            Pageable pageable) {
        return ResponseEntity.ok(classService.getAll(currentUserId, keyword, gradeLevel, pageable));
    }

    @GetMapping("/{id}/tasks/assigned")
    public ResponseEntity<@NonNull Page<@NonNull AssignedTaskResponseDTO>> searchAssignedTaskByClassIdAndTeacherId(
            @PathVariable Long id,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @MyDate(message = "Ngày bắt đầu không hợp lệ") String dateFrom,
            @RequestParam(required = false) @MyDate(message = "Ngày kết thúc không hợp lệ") String dateTo,
            @RequestParam(required = false) @TaskType(message = "Loại bài tập không hợp lệ") String type,
            @RequestParam(required = false) String status,
            @CurrentUserId Long currentUserId,
            Pageable pageable) {
        return ResponseEntity.ok(classService.getAssignedTaskByTeacher(id, currentUserId, keyword, dateFrom, dateTo, type, status, pageable));
    }

    @GetMapping("/{id}/tasks/unassigned")
    public ResponseEntity<@NonNull Page<@NonNull TaskSummaryResponseDTO>> searchUnAssignedTaskByClassIdAndTeacherId(
            @PathVariable Long id,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @MyDate(message = "Ngày bắt đầu không hợp lệ") String dateFrom,
            @RequestParam(required = false) @MyDate(message = "Ngày kết thúc không hợp lệ") String dateTo,
            @RequestParam(required = false) @TaskType(message = "Loại bài tập không hợp lệ") String type,
            @CurrentUserId Long currentUserId,
            Pageable pageable) {
        return ResponseEntity.ok(classService.getUnassignedTaskByTeacher(id, currentUserId, keyword, dateFrom, dateTo, type, pageable));
    }

    @PostMapping("/{id}/tasks/assign")
    public ResponseEntity<@NonNull CommonResponseDTO> assign(
            @PathVariable Long id,
            @RequestBody @Valid AssignTaskRequestDTO request
    ) {
        return ResponseEntity.ok(classService.assignTasks(id, request));
    }

    @DeleteMapping("/{classId}/tasks/{taskId}/assign")
    public ResponseEntity<@NonNull Void> assign(@PathVariable Long classId, @PathVariable Long taskId) {
        classService.unassignTask(classId, taskId);
        return ResponseEntity.noContent().build();
    }
}
