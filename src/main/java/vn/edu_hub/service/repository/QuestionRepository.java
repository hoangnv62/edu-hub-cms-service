package vn.edu_hub.service.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<@NonNull Question, @NonNull Long> {
}
