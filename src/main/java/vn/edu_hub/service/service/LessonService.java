package vn.edu_hub.service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu_hub.service.constants.CommonStatusEnum;
import vn.edu_hub.service.domain.Lesson;
import vn.edu_hub.service.dto.request.CreateLessonRequestDTO;
import vn.edu_hub.service.dto.response.CommonResponseDTO;
import vn.edu_hub.service.repository.LessonRepository;
import vn.edu_hub.service.utils.ResponseUtils;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonService {
    LessonRepository lessonRepository;

    public CommonResponseDTO create(Long subjectId, CreateLessonRequestDTO request) {
        Lesson lesson = Lesson.builder()
                .content(request.content())
                .description(request.description())
                .title(request.title())
                .status(CommonStatusEnum.ACTIVE.getValue())
                .build();
        lessonRepository.save(lesson);

        return ResponseUtils.success();
    }
}
