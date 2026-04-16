package vn.edu_hub.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Embeddable
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassStudentPK {
    @Column(name = "class_id", nullable = false)
    Long classId;
    @Column(name = "student_id", nullable = false)
    Long studentId;
}
