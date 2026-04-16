package vn.edu_hub.service.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_guardians")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentGuardians extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    StudentGuardiansPK id;
}
