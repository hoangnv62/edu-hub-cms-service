package vn.edu_hub.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentCompletionResDTO {
    Integer totalOnTime;
    Integer totalLate;
    Integer totalIncomplete;
}
