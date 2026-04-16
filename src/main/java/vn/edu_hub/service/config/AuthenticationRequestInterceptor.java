package vn.edu_hub.service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthenticationRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return;
        var authHeader = attributes.getRequest().getHeader("Authorization");
        if (StringUtils.hasText(authHeader)) {
            requestTemplate.header("Authorization", authHeader);
        }
    }
}
