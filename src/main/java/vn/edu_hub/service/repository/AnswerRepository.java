package vn.edu_hub.service.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.Answer;

import java.util.Collection;
import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<@NonNull Answer, @NonNull Long> {
    List<Answer> findByQuestionId(Long questionId);

    List<Answer> findByQuestionIdIn(Collection<Long> questionIds);

    void deleteByIdIn(Collection<Long> ids);

    void deleteByQuestionIdIn(Collection<Long> questionIds);
}
