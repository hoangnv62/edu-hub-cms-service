package vn.edu_hub.service.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu_hub.service.constants.ApiResponseCode;
import vn.edu_hub.service.constants.CommonStatusEnum;
import vn.edu_hub.service.constants.GradeLeverEnum;
import vn.edu_hub.service.constants.TaskTypeEnum;
import vn.edu_hub.service.domain.Classes;
import vn.edu_hub.service.domain.TaskClass;
import vn.edu_hub.service.domain.TaskClassPK;
import vn.edu_hub.service.dto.projection.AssignedTaskProjection;
import vn.edu_hub.service.dto.projection.ClassOverviewProjection;
import vn.edu_hub.service.dto.request.AssignTaskRequestDTO;
import vn.edu_hub.service.dto.request.ClassRequestDTO;
import vn.edu_hub.service.dto.response.*;
import vn.edu_hub.service.exception.BusinessException;
import vn.edu_hub.service.repository.ClassRepository;
import vn.edu_hub.service.repository.TaskClassRepository;
import vn.edu_hub.service.repository.TaskRepository;
import vn.edu_hub.service.utils.DateTimeUtils;
import vn.edu_hub.service.utils.ResponseUtils;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassService {
    ClassRepository classRepository;
    TaskClassRepository taskClassRepository;
    TaskRepository taskRepository;

    public CommonResponseDTO create(ClassRequestDTO request, Long currentUserId) {
        if (classRepository.existsByNameIgnoreCaseAndCreatedBy(request.name(), currentUserId)) {
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Tên lớp đã tồn tại");
        }
        GradeLeverEnum gradeLever = GradeLeverEnum.find(request.gradeLevel());
        Classes classes = Classes.builder()
                .name(request.name())
                .gradeLevel(gradeLever.getValue())
                .description(request.description())
                .build();

        classRepository.save(classes);
        return ResponseUtils.success();
    }

    public CommonResponseDTO update(Long classId, ClassRequestDTO request, Long currentUserId) {
        Classes classes = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Lớp học không tồn tại"));

        if (classRepository.existsByNameIgnoreCaseAndIdNotAndCreatedBy(request.name(), classId, currentUserId)) {
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Tên lớp đã tồn tại");
        }

        GradeLeverEnum gradeLever = GradeLeverEnum.find(request.gradeLevel());
        classes.setGradeLevel(gradeLever.getValue());
        classes.setDescription(request.description());
        classes.setName(request.name());

        classRepository.save(classes);
        return ResponseUtils.success();
    }

    public CommonResponseDTO delete(Long classId) {
        validateExists(classId);
        classRepository.deleteById(classId);
        return ResponseUtils.success();
    }

    public ClassDetailResponseDTO getDetail(Long id) {
        Classes classes = classRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Không tìm thấy lớp học"));
        ClassOverviewProjection overview = classRepository.findOverviewById(id);
        GradeLeverEnum grade = GradeLeverEnum.find(classes.getGradeLevel());
        return ClassDetailResponseDTO.builder()
                .id(classes.getId())
                .name(classes.getName())
                .description(classes.getDescription())
                .grade(grade != null ? grade.name() : null)
                .numOfStudents(overview != null ? overview.getTotalStudents() : 0)
                .avgScore(overview != null ? overview.getAvgScore() : null)
                .numOfTasks(overview != null ? overview.getTotalTasks() : 0)
                .build();
    }

    private AssignedTaskResponseDTO convertToAssignedTaskDTO(AssignedTaskProjection p) {
        return AssignedTaskResponseDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .dueDate(p.getDueDate() != null ? p.getDueDate().toEpochMilli() : null)
                .status(p.getStatus() != null ? CommonStatusEnum.find(p.getStatus()).name() : null)
                .completionRate(p.getCompletionRate())
                .build();
    }

    public Page<@NonNull ClassResponseDTO> getAll(Long userId, String keyword, String grade, Pageable pageable) {
        GradeLeverEnum gradeLever = GradeLeverEnum.find(grade);
        Integer gradeValue = gradeLever != null ? gradeLever.getValue() : null;
        return classRepository.searchByCriterial(userId, keyword, gradeValue, pageable)
                .map(ClassResponseDTO::convertToDTO);
    }

    public Page<@NonNull AssignedTaskResponseDTO> getAssignedTaskByTeacher(
            Long classId, Long currentUserId, String keyword, String dateFrom, String dateTo, String type, String status, Pageable pageable
    ) {
        Instant from = DateTimeUtils.toInstantStart(dateFrom);
        Instant to = DateTimeUtils.toInstantEnd(dateTo);
        TaskTypeEnum taskType = TaskTypeEnum.find(type);
        CommonStatusEnum statusEnum = CommonStatusEnum.find(status);
        Integer statusValue = statusEnum != null ? statusEnum.getValue() : null;
        Integer taskTypeValue = taskType != null ? taskType.getValue() : null;
        return taskRepository.searchAssignedTasks(keyword, from, to, taskTypeValue, statusValue, currentUserId, classId, pageable)
                .map(AssignedTaskResponseDTO::fromProjection);
    }

    public Page<@NonNull TaskSummaryResponseDTO> getUnassignedTaskByTeacher(
            Long classId, Long currentUserId, String keyword, String dateFrom, String dateTo, String type, Pageable pageable
    ) {
        Instant from = DateTimeUtils.toInstantStart(dateFrom);
        Instant to = DateTimeUtils.toInstantEnd(dateTo);
        TaskTypeEnum taskType = TaskTypeEnum.find(type);
        Integer taskTypeValue = taskType != null ? taskType.getValue() : null;
        return taskRepository.searchUnassignedTasks(keyword, from, to, taskTypeValue, currentUserId, classId, pageable)
                .map(TaskSummaryResponseDTO::fromProjection);
    }

    public CommonResponseDTO assignTasks(Long classId, AssignTaskRequestDTO requestDTO) {
        validateExists(classId);
        List<TaskClass> taskClasses = requestDTO.getTasks().stream()
                .map(task -> TaskClass.builder()
                        .dueDate(DateTimeUtils.toInstant(task.dueDate()))
                        .id(TaskClassPK.builder()
                                .taskId(task.taskId())
                                .classId(classId)
                                .build())
                        .build())
                .toList();
        taskClassRepository.saveAll(taskClasses);
        return ResponseUtils.success();
    }

    public void unassignTask(Long classId, Long taskId) {
        validateExists(classId);
        taskClassRepository.deleteById_ClassIdAndId_TaskId(classId, taskId);
    }

    private void validateExists(Long classId){
        if (!classRepository.existsById(classId)) {
            throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Bài tập không tồn tại");
        }
    }

}
