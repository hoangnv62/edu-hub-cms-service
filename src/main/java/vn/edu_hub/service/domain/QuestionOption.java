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
@Table(name = "question_options")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionOption extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "question_id", nullable = false)
    Long questionId;

    @Column(name = "label") //A B C D
    String label;

    @Column(name = "content", nullable = false)
    String content;

    @Column(name = "is_correct", nullable = false)
    boolean isCorrect = false;
}
