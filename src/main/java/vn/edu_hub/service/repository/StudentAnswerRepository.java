package vn.edu_hub.service.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.StudentAnswer;

@Repository
public interface StudentAnswerRepository extends JpaRepository<@NonNull StudentAnswer, @NonNull Long> {
}
