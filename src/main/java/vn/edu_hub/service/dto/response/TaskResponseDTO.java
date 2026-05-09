package vn.edu_hub.service.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.edu_hub.service.dto.projection.TaskProjection;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskResponseDTO {
    Long id;
    String name;
    String description;
    Integer assignedClassCount;
}
