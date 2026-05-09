package vn.edu_hub.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskClassPK implements Serializable {

    @Column(name = "task_id", nullable = false)
    Long taskId;

    @Column(name = "class_id", nullable = false)
    Long classId;
}
