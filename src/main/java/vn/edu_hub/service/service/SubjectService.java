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
import vn.edu_hub.service.domain.Subject;
import vn.edu_hub.service.dto.request.SubjectRequestDTO;
import vn.edu_hub.service.dto.response.CommonResponseDTO;
import vn.edu_hub.service.dto.response.SubjectResponseDTO;
import vn.edu_hub.service.exception.BusinessException;
import vn.edu_hub.service.repository.ClassSubjectRepository;
import vn.edu_hub.service.repository.SubjectRepository;
import vn.edu_hub.service.utils.ResponseUtils;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectService {
    SubjectRepository subjectRepository;
    ClassSubjectRepository classSubjectRepository;

    public CommonResponseDTO create(SubjectRequestDTO requestDTO) {
        if (subjectRepository.existsByNameIgnoreCase(requestDTO.name())) {
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Tên môn học đã tồn tại");
        }
        Subject subject = Subject.builder()
                .name(requestDTO.name())
                .build();
        subjectRepository.save(subject);
        return ResponseUtils.success();
    }

    public CommonResponseDTO update(Long id, SubjectRequestDTO requestDTO) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Không tìm thấy môn học"));
        if (subjectRepository.existsByNameIgnoreCaseAndIdNot(requestDTO.name(), id)) {
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Tên môn học đã tồn tại");
        }
        subject.setName(requestDTO.name());
        subjectRepository.save(subject);
        return ResponseUtils.success();
    }

    public Page<@NonNull SubjectResponseDTO> findAll(String name, Pageable pageable) {
        return subjectRepository.findByNameIgnoreCase(name, pageable)
                .map(e -> SubjectResponseDTO.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .build());
    }

    public void deleteById(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Không tìm thấy môn học");
        }
        classSubjectRepository.deleteByIdSubjectId(id);
        subjectRepository.deleteById(id);
    }
}
