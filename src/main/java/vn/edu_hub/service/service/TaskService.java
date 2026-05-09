package vn.edu_hub.service.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu_hub.service.constants.ApiResponseCode;
import vn.edu_hub.service.constants.TaskTypeEnum;
import vn.edu_hub.service.domain.Task;
import vn.edu_hub.service.domain.TaskClass;
import vn.edu_hub.service.domain.TaskClassPK;
import vn.edu_hub.service.dto.projection.TaskProjection;
import vn.edu_hub.service.dto.request.TaskClassRequestDTO;
import vn.edu_hub.service.dto.request.TaskRequestDTO;
import vn.edu_hub.service.dto.response.CommonResponseDTO;
import vn.edu_hub.service.dto.response.TaskResponseDTO;
import vn.edu_hub.service.exception.BusinessException;
import vn.edu_hub.service.repository.TaskClassRepository;
import vn.edu_hub.service.repository.TaskRepository;
import vn.edu_hub.service.utils.DateTimeUtils;
import vn.edu_hub.service.utils.ResponseUtils;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {
    TaskRepository taskRepository;
    TaskClassRepository taskClassRepository;

    public Page<@NonNull TaskResponseDTO> searchByCriterial(String keyword, String dateFrom, String dateTo, Long currentUserId, Pageable pageable) {
        Instant from = DateTimeUtils.toInstantStart(dateFrom);
        Instant to = DateTimeUtils.toInstantEnd(dateTo);
        return taskRepository.searchByCriterial(keyword, from, to, currentUserId, pageable)
                .map(this::convertToDTO);
    }

    public CommonResponseDTO create(TaskRequestDTO request, Long userId) {
        if (taskRepository.existsByNameIgnoreCaseAndCreatedBy(request.name(), userId)) {
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Bài tập đã tồn tại");
        }
        Task task = Task.builder()
                .name(request.name())
                .description(request.description())
                .type(TaskTypeEnum.find(request.type()).getValue())
                .build();
        taskRepository.save(task);
        return ResponseUtils.success();
    }

    public CommonResponseDTO update(Long id, TaskRequestDTO request, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Không tìm thấy bài tập"));
        if (taskRepository.existsByNameIgnoreCaseAndCreatedByAndIdNot(request.name(), userId, id)) {
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Bài tập đã tồn tại");
        }
        task.setName(request.name());
        task.setDescription(request.description());
        taskRepository.save(task);
        return ResponseUtils.success();
    }

    public CommonResponseDTO assignForClass(Long taskId, TaskClassRequestDTO requestDTO) {
        if (!taskRepository.existsById(taskId)) {
            throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Bài tập không tồn tại");
        }
        List<TaskClass> taskClasses = requestDTO.classIds().stream()
                .map(classId -> TaskClass.builder()
                        .dueDate(DateTimeUtils.toInstant(requestDTO.dueDate()))
                        .id(TaskClassPK.builder()
                                .taskId(taskId)
                                .classId(classId)
                                .build())
                        .build())
                .toList();
        taskClassRepository.saveAll(taskClasses);
        return ResponseUtils.success();
    }

    public void unassignForClass(Long taskId, List<Long> classIds) {
        if (!taskRepository.existsById(taskId)) {
            throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Bài tập không tồn tại");
        }
        taskClassRepository.deleteByTaskIdAndClassIdIn(taskId, classIds);
    }

    public void deleteByIds(List<Long> ids) {
        taskClassRepository.deleteByTaskIdIn(ids);
        taskRepository.deleteByIdIn(ids);
    }

    private TaskResponseDTO convertToDTO(TaskProjection projection) {
        return TaskResponseDTO.builder()
                .id(projection.getId())
                .name(projection.getName())
                .description(projection.getDescription())
                .assignedClassCount(projection.getAssignedClassCount())
                .build();
    }
}
