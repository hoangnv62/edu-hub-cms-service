package vn.edu_hub.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.edu_hub.service.constants.GradeLeverEnum;
import vn.edu_hub.service.dto.projection.ClassProjection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassResponseDTO {
    Long id;
    String name;
    String description;
    String grade;
    Integer numOfStudents;

    public static ClassResponseDTO convertToDTO(ClassProjection projection) {
        GradeLeverEnum gradeLeverEnum = GradeLeverEnum.find(projection.getGradeLevel());
        return ClassResponseDTO.builder()
                .id(projection.getId())
                .name(projection.getName())
                .description(projection.getDescription())
                .grade(gradeLeverEnum.name())
                .numOfStudents(projection.getNumOfStudents())
                .build();
    }
}