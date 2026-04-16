package vn.edu_hub.service.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.StudentGuardians;

@Repository
public interface StudentGuardiansRepository extends JpaRepository<@NonNull StudentGuardians, @NonNull Long> {
}
