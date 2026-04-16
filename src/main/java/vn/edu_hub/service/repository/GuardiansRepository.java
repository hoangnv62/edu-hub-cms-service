package vn.edu_hub.service.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu_hub.service.domain.Guardians;

@Repository
public interface GuardiansRepository extends JpaRepository<@NonNull Guardians, @NonNull Long> {
}
