package vn.edu_hub.service.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lesson_media")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonMedia {
    @EmbeddedId
    LessonMediaPK id;
}
