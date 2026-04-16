package vn.edu_hub.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Embeddable
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonMediaPK {

    @Column(name = "lesson_id")
    Long lessonId;
    @Column(name = "media_id")
    Long mediaId;
}
