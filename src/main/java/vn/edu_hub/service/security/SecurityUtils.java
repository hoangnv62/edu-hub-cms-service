package vn.edu_hub.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public final class SecurityUtils {
    private SecurityUtils() {
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
               && authentication.getAuthorities().stream()
                       .anyMatch(authority -> Arrays.asList(authorities).contains(authority));
    }

    public static Optional<Long> getCurrentUserIdLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.of(Objects.requireNonNull(securityContext.getAuthentication()).getAuthorities()) // getAuthorities(): tập hợp quyền ((GrantedAuthority)) của người dùng
                .map(auth -> {
                    if (auth instanceof AuthenticationToken authenticationToken) {
                        return authenticationToken.getUserId();
                    }
                    return null;
                });
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (authentication.getPrincipal() instanceof String principal) {
            return principal;
        } else {
            return null;
        }
    }
}
