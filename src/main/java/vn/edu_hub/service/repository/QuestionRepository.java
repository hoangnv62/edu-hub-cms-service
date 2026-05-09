package vn.edu_hub.service.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<@NonNull Question, @NonNull Long> {
    boolean existsByPositionAndTaskId(Integer position, Long taskId);

    boolean existsByPositionAndTaskIdAndIdNot(Integer position, Long taskId, Long id);
    List<Question> findByTaskIdAndIdIn(Long taskId, List<Long> ids);

    List<Question> findByTaskId(Long taskId);
    Page<@NonNull Question> findByTaskId(Long taskId, Pageable pageable);
    void deleteByIdIn(@NonNull List<Long> ids);
}
