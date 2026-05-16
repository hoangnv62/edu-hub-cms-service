package vn.edu_hub.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.edu_hub.service.constants.CommonStatusEnum;
import vn.edu_hub.service.constants.TaskTypeEnum;
import vn.edu_hub.service.dto.projection.AssignedTaskProjection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignedTaskResponseDTO {
    Long id;
    String name;
    String description;
    Long dueDate;
    String status;
    Float avgScore;
    Float completionRate;
    String type;
    Integer totalQuestions;
    Long dateCreated;

    public static AssignedTaskResponseDTO fromProjection(AssignedTaskProjection projection) {
        return AssignedTaskResponseDTO.builder()
                .id(projection.getId())
                .name(projection.getName())
                .description(projection.getDescription())
                .dueDate(projection.getDueDate().toEpochMilli())
                .dateCreated(projection.getDateCreated().toEpochMilli())
                .status(CommonStatusEnum.find(projection.getStatus()).name())
                .completionRate(projection.getCompletionRate())
                .totalQuestions(projection.getTotalQuestions())
                .type(TaskTypeEnum.find(projection.getType()).name())
                .avgScore(projection.getAvgScore())
                .build();
    }
}
