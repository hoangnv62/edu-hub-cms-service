package vn.edu_hub.service.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.ClassStudent;
import vn.edu_hub.service.domain.ClassStudentPK;

@Repository
public interface ClassStudentRepository extends JpaRepository<@NonNull ClassStudent, @NonNull ClassStudentPK> {
}
