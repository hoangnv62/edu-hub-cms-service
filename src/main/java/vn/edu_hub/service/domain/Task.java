package vn.edu_hub.service.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "subject_id", nullable = false)
    Long subjectId;

    @Column(name = "class_id", nullable = false)
    Long classId;

    @Column(name = "teacher_id", nullable = false)
    Long teacherId;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "due_date")
    Instant dueDate;

    @Column(name = "type")
    Integer type;
}
