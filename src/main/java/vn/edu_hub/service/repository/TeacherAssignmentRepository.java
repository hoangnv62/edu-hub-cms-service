package vn.edu_hub.service.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.TeacherAssignment;

@Repository
public interface TeacherAssignmentRepository extends JpaRepository<@NonNull TeacherAssignment, @NonNull Long> {
}
