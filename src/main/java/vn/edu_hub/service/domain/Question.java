package vn.edu_hub.service.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "questions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "task_id")
    Long taskId;

    @Column(name = "content", nullable = false)
    String content;

    @Column(name = "max_score", nullable = false)
    Float maxScore = 1f;

    Integer type;
}
