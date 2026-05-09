package vn.edu_hub.service.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.Lesson;
@Repository
public interface LessonRepository extends JpaRepository<@NonNull Lesson,@NonNull Long> {
    boolean existsByTitleIgnoreCase(@NonNull String title);
}
