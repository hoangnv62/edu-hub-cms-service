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
@Table(name = "submissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Submission extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "task_id", nullable = false)
    Long taskId;

    @Column(name = "student_id", nullable = false)
    Long studentId;

    @Column(name = "total_score", nullable = false)
    Float totalScore;

    @Column(name = "teacher_feedback")
    String teacherFeedback;

    @Column(name = "status", nullable = false)
    Integer status;
}
