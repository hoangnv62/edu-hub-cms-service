package vn.edu_hub.service.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.edu_hub.service.domain.TaskClass;
import vn.edu_hub.service.domain.TaskClassPK;
import vn.edu_hub.service.dto.projection.AssignedTaskProjection;

import java.util.List;

@Repository
public interface TaskClassRepository extends JpaRepository<@NonNull TaskClass, @NonNull TaskClassPK> {
    @Query("""
                SELECT tc.id.taskId AS id,
                       t.name AS name,
                       tc.dueDate AS dueDate,
                       t.status AS status,
                       CASE WHEN COUNT(DISTINCT cs.id.studentId) = 0
                            THEN 0.0
                            ELSE COUNT(DISTINCT s.studentId) * 1.0 / COUNT(DISTINCT cs.id.studentId)
                       END AS completionRate
                FROM TaskClass tc
                JOIN Task t ON t.id = tc.id.taskId
                LEFT JOIN ClassStudent cs ON cs.id.classId = tc.id.classId
                LEFT JOIN Submission s ON s.taskId = tc.id.taskId AND s.studentId = cs.id.studentId
                WHERE tc.id.classId = :classId
                GROUP BY tc.id.taskId, t.name, tc.dueDate, t.status
            """)
    List<AssignedTaskProjection> findAssignedTasksByClassId(@Param("classId") Long classId);

    @Modifying
    @Transactional
    @Query(value = """
                DELETE FROM TaskClass tc WHERE tc.id.taskId = :taskId AND tc.id.classId IN :classIds
            """)
    void deleteByTaskIdAndClassIdIn(@Param("taskId") Long taskId, @Param("classIds") List<Long> ids);

    void deleteById_ClassIdAndId_TaskId(Long idClassId, Long idTaskId);
    @Modifying
    @Transactional
    @Query(value = """
                DELETE FROM TaskClass tc WHERE tc.id.taskId IN :taskIds
            """)
    void deleteByTaskIdIn(@Param("taskIds") List<Long> ids);
}
