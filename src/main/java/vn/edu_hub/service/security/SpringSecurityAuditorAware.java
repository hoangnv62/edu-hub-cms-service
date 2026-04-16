package vn.edu_hub.service.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import vn.edu_hub.service.constants.ConstantsAll;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserIdLogin().orElse(ConstantsAll.SYSTEM_ID));
    }
}
