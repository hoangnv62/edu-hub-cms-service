package vn.edu_hub.service.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.Submission;

@Repository
public interface SubmissionRepository extends JpaRepository<@NonNull Submission, @NonNull Long> {
}
