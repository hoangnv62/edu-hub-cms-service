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
import vn.edu_hub.service.dto.projection.AssignedTaskProjection;
import vn.edu_hub.service.dto.projection.TaskProjection;
import vn.edu_hub.service.dto.projection.TaskSummaryProjection;

@Repository
public interface TaskRepository extends JpaRepository<@NonNull Task, @NonNull Long> {
    void deleteByIdIn(@NonNull List<@NonNull Long> ids);

    boolean existsByNameIgnoreCaseAndCreatedBy(@NonNull String name, @NonNull Long createdBy);

    boolean existsByNameIgnoreCaseAndCreatedByAndIdNot(@NonNull String name, @NonNull Long createdBy, @NonNull Long id);

    @Query("""
                SELECT  t.id AS id,
                        t.name AS name,
                        t.description AS description,
                        t.type AS type,
                        t.status AS status,
                        t.dateCreated AS dateCreated,
                        COUNT(q.id) AS totalQuestions
                FROM Task t
                LEFT JOIN Question q ON q.taskId = t.id
                WHERE t.id = :id
                GROUP BY t.id, t.name, t.description, t.type, t.status, t.dateCreated
            """)
    TaskSummaryProjection findSummaryById(@Param("id") Long id);

    @Query(value = """
                SELECT  t.id AS id,
                        t.name AS name,
                        t.description AS description,
                        t.type AS type,
                        t.status AS status,
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

    @Query("""
            SELECT
                t.id AS id,
                t.name AS name,
                t.description AS description,
                tc.dueDate AS dueDate,
                t.status AS status,
                AVG(s.totalScore) AS avgScore,
                CASE
                    WHEN COUNT(DISTINCT cs.id.studentId) = 0
                    THEN 0.0F
                    ELSE COUNT(DISTINCT s.studentId) * 1.0F / COUNT(DISTINCT cs.id.studentId)
                END AS completionRate,
                t.type AS type,
                (
                    SELECT COUNT(q.id)
                    FROM Question q
                    WHERE q.taskId = t.id
                ) AS totalQuestions,
                t.dateCreated AS dateCreated
            FROM Task t
            JOIN TaskClass tc ON tc.id.taskId = t.id
            LEFT JOIN Classes c ON c.id = tc.id.classId
            LEFT JOIN ClassStudent cs ON cs.id.classId = c.id
            LEFT JOIN Submission s
                   ON s.taskId = t.id
                  AND s.studentId = cs.id.studentId
            WHERE t.status <> -1
              AND (:keyword IS NULL OR :keyword = '' OR t.name LIKE CONCAT('%', :keyword, '%'))
              AND (:from IS NULL OR t.dateCreated >= :from)
              AND (:to IS NULL OR t.dateCreated <= :to)
              AND (:type IS NULL OR t.type = :type)
              AND (:status IS NULL OR t.status = :status)
              AND t.createdBy = :currentUserId
              AND c.id = :classId
            
            GROUP BY
                t.id,
                t.name,
                t.description,
                t.status,
                t.type,
                t.dateCreated
            """)
    Page<@NonNull AssignedTaskProjection> searchAssignedTasks(
            @Param("keyword") String keyword,
            @Param("from") Instant from,
            @Param("to") Instant to,
            @Param("type") Integer type,
            @Param("status") Integer status,
            @Param("currentUserId") Long currentUserId,
            @Param("classId") Long classId,
            Pageable pageable
    );

    @Query("""
            SELECT
                t.id AS id,
                t.name AS name,
                t.description AS description,
                t.type AS type,
                t.status AS status,
                t.dateCreated AS dateCreated,
                COUNT(q.id) AS totalQuestions
            FROM Task t
            LEFT JOIN Question q
                   ON q.taskId = t.id
            WHERE t.status = 1
              AND (:keyword IS NULL OR :keyword = '' OR t.name LIKE CONCAT('%', :keyword, '%'))
              AND (:from IS NULL OR t.dateCreated >= :from)
              AND (:to IS NULL OR t.dateCreated <= :to)
              AND (:type IS NULL OR t.type = :type)
              AND t.createdBy = :currentUserId
              AND NOT EXISTS (
                    SELECT 1
                    FROM TaskClass tc
                    WHERE tc.id.taskId = t.id
                      AND tc.id.classId = :classId
              )
            GROUP BY
                t.id,
                t.name,
                t.description,
                t.type,
                t.status,
                t.dateCreated
            """)
    Page<@NonNull TaskSummaryProjection> searchUnassignedTasks(
            @Param("keyword") String keyword,
            @Param("from") Instant from,
            @Param("to") Instant to,
            @Param("type") Integer type,
            @Param("currentUserId") Long currentUserId,
            @Param("classId") Long classId,
            Pageable pageable
    );
}
