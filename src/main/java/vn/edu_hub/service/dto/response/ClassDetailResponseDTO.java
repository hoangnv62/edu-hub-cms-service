package vn.edu_hub.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassDetailResponseDTO {
    Long id;
    String name;
    String description;
    String grade;
    Integer numOfStudents;
    Integer numOfTasks;
    Float avgScore;
}
