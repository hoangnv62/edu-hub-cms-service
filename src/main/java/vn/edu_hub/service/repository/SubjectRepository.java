package vn.edu_hub.service.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<@NonNull Subject, @NonNull Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    Page<@NonNull Subject> findByNameIgnoreCase(String name, Pageable pageable);
}
