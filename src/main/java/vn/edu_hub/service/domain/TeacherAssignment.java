package vn.edu_hub.service.domain;

import jakarta.persistence.*;
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
@Table(name = "teacher_assignments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"teacher_id", "class_id"})
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherAssignment extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "teacher_id", nullable = false)
    Long teacherId;

    @Column(name = "class_id", nullable = false)
    Long classId;
}
