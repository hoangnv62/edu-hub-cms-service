package vn.edu_hub.service.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.ClassSubject;
import vn.edu_hub.service.domain.ClassSubjectPK;

@Repository
public interface ClassSubjectRepository extends JpaRepository<@NonNull ClassSubject,@NonNull ClassSubjectPK> {
    void deleteById(@NonNull ClassSubjectPK classSubjectPK);
    void deleteByIdClassId(Long id_classId);
    void deleteByIdSubjectId(Long id_subjectId);
}
