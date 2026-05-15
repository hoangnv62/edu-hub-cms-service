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
                        t.type AS type,
                        t.status AS status,
                        t.dueTime AS dueDate,
                        t.dateCreated AS dateCreated,
                        COUNT(DISTINCT tc.id.classId) AS assignedClassCount,
                        AVG(s.totalScore) AS avgScore
                     FROM Task t
                     LEFT JOIN TaskClass tc ON t.id = tc.id.taskId
                     LEFT JOIN Submission s ON s.taskId = t.id
                     WHERE (:keyword IS NULL OR :keyword = '' OR t.name LIKE CONCAT('%',:keyword,'%')) AND
                           (:from IS NULL OR t.dateCreated >= :from) AND
                           (:to IS NULL OR t.dateCreated <= :to) AND
                           (:type IS NULL OR t.type = :type) AND
                           t.createdBy = :currentUserId
                     GROUP BY t.id, t.name, t.description, t.type, t.dateCreated, t.dateUpdated
                     ORDER BY t.dateUpdated DESC
            """)
    Page<@NonNull TaskProjection> searchByCriterial(
            @Param("keyword") String keyword,
            @Param("from") Instant from,
            @Param("to") Instant to,
            @Param("type") Integer type,
            @Param("currentUserId") Long currentUserId,
            Pageable pageable);
}
