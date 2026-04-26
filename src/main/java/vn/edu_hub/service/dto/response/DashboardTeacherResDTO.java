package vn.edu_hub.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardTeacherResDTO {
    SummaryResponseDTO summary;
    AssignmentCompletionResDTO assignmentCompletion;
    ScoreDistributionResponseDTO scoreDistribution;
    AssignmentTrendResponseDTO assignmentTrend;
}
