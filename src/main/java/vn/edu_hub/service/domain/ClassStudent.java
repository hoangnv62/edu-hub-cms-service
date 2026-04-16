package vn.edu_hub.service.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "class_student")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassStudent extends AbstractAuditingEntity implements Serializable {

    @EmbeddedId
    ClassStudentPK id;
}
