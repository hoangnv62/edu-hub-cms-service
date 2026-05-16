package vn.edu_hub.service.dto.projection;

import java.time.Instant;

public interface AssignedTaskProjection {
    Long getId();

    String getName();

    String getDescription();

    Instant getDueDate();

    Integer getStatus();

    Float getAvgScore();

    Float getCompletionRate();

    Integer getType();

    Integer getTotalQuestions();

    Instant getDateCreated();
}
