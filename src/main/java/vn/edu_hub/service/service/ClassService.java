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
import vn.edu_hub.service.constants.GradeLeverEnum;
import vn.edu_hub.service.domain.Classes;
import vn.edu_hub.service.dto.request.ClassRequestDTO;
import vn.edu_hub.service.dto.response.ClassDetailResponseDTO;
import vn.edu_hub.service.dto.response.ClassResponseDTO;
import vn.edu_hub.service.dto.response.CommonResponseDTO;
import vn.edu_hub.service.exception.BusinessException;
import vn.edu_hub.service.repository.ClassRepository;
import vn.edu_hub.service.utils.ResponseUtils;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassService {
    ClassRepository classRepository;

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
        if (!classRepository.existsById(classId)) {
            throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Lớp học không tồn tại");
        }
        classRepository.deleteById(classId);
        return ResponseUtils.success();
    }

    public ClassResponseDTO getDetail(Long id) {
        Classes classes = classRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Không tìm thấy lớp học"));
        return ClassResponseDTO.builder()
                .id(id)
                .name(classes.getName())
                .description(classes.getDescription())
                .build();
    }

    public Page<@NonNull ClassResponseDTO> getAll(Long userId, String keyword, String grade, Pageable pageable) {
        GradeLeverEnum gradeLever = GradeLeverEnum.find(grade);
        Integer gradeValue = gradeLever != null ? gradeLever.getValue() : null;
        return classRepository.searchByCriterial(userId, keyword, gradeValue, pageable)
                .map(ClassResponseDTO::convertToDTO);
    }
}
