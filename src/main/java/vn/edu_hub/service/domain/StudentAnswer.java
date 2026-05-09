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
@Table(name = "student_answer")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentAnswer extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "question_id", nullable = false)
    Long questionId;

    @Column(name = "selected_answer_id")
    Long selectedAnswerId;

    @Column(name = "score", nullable = false)
    Float score;

    @Column(name = "is_correct")
    boolean isCorrect;
}
