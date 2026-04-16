package vn.edu_hub.service.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationToken extends UsernamePasswordAuthenticationToken {
    @Serial
    private static final long serialVersionUID = 1L;

    String token;
    List<String> scopes;
    List<String> roles;
    Long userId;
    String username;

    public AuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AuthenticationToken(Object principal, Object credentials, String token) {
        super(principal, credentials);
        this.token = token;
    }

    public AuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public AuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String token) {
        super(principal, credentials, authorities);
        this.token = token;
    }
}
