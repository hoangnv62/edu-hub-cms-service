package vn.edu_hub.service.dto.projection;

import java.time.Instant;

public interface TaskSummaryProjection {
    Long getId();
    String getName();
    String getDescription();
    Integer getType();
    Integer getStatus();
    Instant getDateCreated();
    Instant getDueDate();
    Integer getTotalQuestions();
}
