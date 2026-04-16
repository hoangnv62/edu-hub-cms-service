package vn.edu_hub.service.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu_hub.service.domain.LessonMedia;
import vn.edu_hub.service.domain.LessonMediaPK;

public interface LessonMediaRepository extends JpaRepository<@NonNull LessonMedia, @NonNull LessonMediaPK> {
}
