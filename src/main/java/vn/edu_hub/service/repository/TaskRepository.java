package vn.edu_hub.service.repository;

import lombok.NonNull;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.Task;
import vn.edu_hub.service.dto.projection.TaskProjection;

@Repository
public interface TaskRepository extends JpaRepository<@NonNull Task, @NonNull Long> {
    void deleteByIdIn(@NonNull List<@NonNull Long> ids);
    boolean existsByNameIgnoreCaseAndCreatedBy(@NonNull String name, @NonNull Long createdBy);
    boolean existsByNameIgnoreCaseAndCreatedByAndIdNot(@NonNull String name, @NonNull Long createdBy, @NonNull Long id);

    @Query(value = """
                SELECT  t.id AS id,
                        t.name AS name,
                        t.description AS description,
                        t.type AS type
                     FROM Task t LEFT JOIN TaskClass tc ON t.id = tc.id.taskId
                     WHERE (:keyword IS NULL OR :keyword = '' OR t.name LIKE CONCAT('%',:keyword,'%')) AND
                           (:from IS NULL OR :from = '' OR t.dateCreated >= :from) AND
                           (:to IS NULL OR :to = '' OR t.dateCreated <= :to) AND
                           t.createdBy = :currentUserId
            """)
    Page<@NonNull TaskProjection> searchByCriterial(
            @Param("keyword") String keyword,
            @Param("from") Instant from,
            @Param("to") Instant to,
            @Param("currentUserId") Long currentUserId,
            Pageable pageable);
}
