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
@Table(name = "task_class")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskClass extends AbstractAuditingEntity implements Serializable {

    @EmbeddedId
    TaskClassPK id;

    @Column(name = "due_date")
    Instant dueDate;
}
