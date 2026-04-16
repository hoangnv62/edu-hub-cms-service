package vn.edu_hub.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentGuardiansPK implements Serializable {

    @Column(name = "student_id")
    Long studentId;

    @Column(name = "guardians_id")
    Long guardiansId;
}
