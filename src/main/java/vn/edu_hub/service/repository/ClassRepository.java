package vn.edu_hub.service.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.Classes;
import vn.edu_hub.service.dto.projection.ClassProjection;

@Repository
public interface ClassRepository extends JpaRepository<@NonNull Classes, @NonNull Long> {
    boolean existsByNameIgnoreCaseAndCreatedBy(String name, Long createdBy);

    boolean existsByNameIgnoreCaseAndIdNotAndCreatedBy(String name, Long id, Long createdBy);

    @Query(value = """
            SELECT c.id AS id,
                   c.name AS name,
                   c.description AS description,
                   c.gradeLevel AS gradeLevel,
                   COUNT(cs.id.studentId) AS numOfStudents
            FROM Classes c
            LEFT JOIN ClassStudent cs ON c.id = cs.id.classId
            WHERE c.createdBy = :createdBy
                AND (:grade IS NULL OR c.gradeLevel = :grade)
                AND (:keyword IS NULL OR :keyword = '' OR c.name LIKE CONCAT('%', :keyword, '%'))
            GROUP BY c.id, c.gradeLevel, c.name, c.description
            ORDER BY c.gradeLevel DESC, c.name ASC
            """)
    Page<@NonNull ClassProjection> searchByCriterial(
            @Param("createdBy") Long createdBy,
            @Param("keyword") String keyword,
            @Param("grade") Integer grade,
            Pageable pageable);
}
