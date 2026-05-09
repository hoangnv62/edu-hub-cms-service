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

import java.util.List;

@Repository
public interface TaskClassRepository extends JpaRepository<@NonNull TaskClass, @NonNull TaskClassPK> {
    @Modifying
    @Transactional
    @Query(value = """
                DELETE FROM TaskClass tc WHERE tc.id.taskId = :taskId AND tc.id.classId IN :classIds
            """)
    void deleteByTaskIdAndClassIdIn(@Param("taskId") Long taskId, @Param("classIds") List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = """
                DELETE FROM TaskClass tc WHERE tc.id.taskId IN :taskIds
            """)
    void deleteByTaskIdIn(@Param("taskIds") List<Long> ids);
}
