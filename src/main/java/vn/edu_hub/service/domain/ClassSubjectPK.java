package vn.edu_hub.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassSubjectPK {
    @Column(name = "class_id", nullable = false)
    Long classId;
    @Column(name = "subject_id", nullable = false)
    Long subjectId;
}
