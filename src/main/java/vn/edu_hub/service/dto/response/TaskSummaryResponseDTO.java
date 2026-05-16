package vn.edu_hub.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.edu_hub.service.constants.CommonStatusEnum;
import vn.edu_hub.service.constants.TaskTypeEnum;
import vn.edu_hub.service.dto.projection.TaskSummaryProjection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskSummaryResponseDTO {
    Long id;
    String name;
    String description;
    String status;
    Long dateCreated;
    Long dueDate;
    String type;
    Integer totalQuestions;

    public static TaskSummaryResponseDTO fromProjection(TaskSummaryProjection projection) {
        return TaskSummaryResponseDTO.builder()
                .id(projection.getId())
                .name(projection.getName())
                .description(projection.getDescription())
                .type(projection.getType() != null ? TaskTypeEnum.find(projection.getType()).name() : null)
                .status(projection.getStatus() != null ? CommonStatusEnum.find(projection.getStatus()).name() : null)
                .dateCreated(projection.getDateCreated() != null ? projection.getDateCreated().toEpochMilli() : null)
                .dueDate(projection.getDueDate() != null ? projection.getDueDate().toEpochMilli() : null)
                .totalQuestions(projection.getTotalQuestions())
                .build();
    }
}
