package vn.edu_hub.service.dto.projection;

import java.time.Instant;

public interface TaskProjection {
    Long getId();
    String getName();
    String getDescription();
    Integer getType();
    Integer getAssignedClassCount();
    Integer getStatus();
    Float getAvgScore();
    Instant getDateCreated();
    Instant getDueDate();
}
