package vn.edu_hub.service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.edu_hub.service.dto.request.TaskRequestDTO;
import vn.edu_hub.service.dto.response.CommonResponseDTO;
import vn.edu_hub.service.repository.TaskRepository;
import vn.edu_hub.service.utils.ResponseUtils;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {
    TaskRepository taskRepository;

    public CommonResponseDTO create(TaskRequestDTO request) {
        // Implementation for creating a task
        return ResponseUtils.success();
    }

    public CommonResponseDTO update(Long id, TaskRequestDTO request) {
        // Implementation for updating a task
        return ResponseUtils.success();
    }

    public CommonResponseDTO assignForClass(Long taskId, Long classId) {
        // Implementation for assigning a task to a class
        return ResponseUtils.success();
    }

    public void deleteByIds(List<Long> ids) {
        taskRepository.deleteByIdIn(ids);
    }
}
