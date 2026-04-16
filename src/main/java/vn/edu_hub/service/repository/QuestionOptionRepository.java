package vn.edu_hub.service.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.QuestionOption;

@Repository
public interface QuestionOptionRepository extends JpaRepository<@NonNull QuestionOption,@NonNull Long> {
}
