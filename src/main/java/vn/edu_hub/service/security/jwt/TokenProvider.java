package vn.edu_hub.service.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tech.jhipster.config.JHipsterProperties;
import vn.edu_hub.service.constants.Authorities;
import vn.edu_hub.service.exception.UnauthenticatedException;
import vn.edu_hub.service.security.AuthenticationToken;
import vn.edu_hub.service.security.IBEUser;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.*;

@Slf4j
@Getter
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String CREATED_KEY = "created";
    private static final String USER_ID_KEY = "user_id";
    private static final String USERNAME_KEY = "username";
    private static final String INVALID_JWT_TOKEN = "Invalid JWT token";

    private final Key key;
    private final JwtParser jwtParser;
    private final long tokenValidityInMilliseconds;
    private final long tokenValidityInMillisecondsForRememberMe;

    public TokenProvider(JHipsterProperties jHipsterProperties) {
        byte[] keyBytes;
        String base64Secret = jHipsterProperties.getSecurity()
                .getAuthentication()
                .getJwt()
                .getBase64Secret();
        if (StringUtils.hasText(base64Secret)) {
            log.info("Using JWT token base64 secret");
            keyBytes = Decoders.BASE64.decode(base64Secret);
        } else {
            log.warn("JWT key is not Base64-encoded. Using plain secret.");
            String secret = jHipsterProperties.getSecurity()
                    .getAuthentication()
                    .getJwt()
                    .getSecret();
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.key = secretKey;

        this.jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        this.tokenValidityInMilliseconds = 1000L *
                                           jHipsterProperties.getSecurity()
                                                   .getAuthentication()
                                                   .getJwt()
                                                   .getTokenValidityInSeconds();

        this.tokenValidityInMillisecondsForRememberMe = 1000L *
                                                        jHipsterProperties.getSecurity()
                                                                .getAuthentication()
                                                                .getJwt()
                                                                .getTokenValidityInSecondsForRememberMe();
    }

    public String generateToken(Long userId, String username, Integer role, boolean rememberMe) {
        long now = (new Date()).getTime();
        long tokenTime = rememberMe ? this.tokenValidityInMillisecondsForRememberMe : this.tokenValidityInMilliseconds;
        Date expired = new Date(now + tokenTime);
        Authorities authorities = Authorities.find(role);
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_KEY, authorities);
        claims.put(USER_ID_KEY, userId);
        claims.put(USERNAME_KEY, username);
        claims.put(CREATED_KEY, Instant.now().getEpochSecond());
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(now))
                .expiration(expired)
                .signWith(key)
                .subject(username)
                .compact();
    }

    public Authentication getAuthentication(String token) throws UnauthenticatedException {
        try {
            Claims claims = jwtParser
                    .parseSignedClaims(token)
                    .getPayload();
            // trả về danh sách quyền của user
            Collection<? extends GrantedAuthority> authorities = Arrays
                    .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .filter(auth -> !auth.trim().isEmpty())
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            String userId = claims.get(USER_ID_KEY).toString();
            if (io.micrometer.common.util.StringUtils.isBlank(userId)) {
                log.info("Invalid JWT signature. user_id null");
                throw new UnauthenticatedException();
            }
            String username = claims.getSubject();
            IBEUser principal = new IBEUser(Long.valueOf(userId), username, "", authorities);
            AuthenticationToken authenticationToken = new AuthenticationToken(principal, "", authorities, token);
            authenticationToken.setDetails("pre_auth");
            authenticationToken.setUserId(Long.valueOf(userId));

            if (claims.containsKey(AUTHORITIES_KEY) && io.micrometer.common.util.StringUtils.isNotBlank(claims.get(AUTHORITIES_KEY).toString())) {
                String jwtRoles = claims.get(AUTHORITIES_KEY).toString();
                authenticationToken.setRoles(Arrays.asList(jwtRoles.split(",")));
            }

            return authenticationToken;
        } catch (Exception e) {
            log.info("Invalid JWT signature: {}", e.getMessage());
            log.trace("Invalid JWT signature tract: {}", ExceptionUtils.getStackTrace(e));
            throw new UnauthenticatedException();
        }
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseSignedClaims(authToken);// phân tích token thành header, payload(claims) và signature
            return true;
        } catch (ExpiredJwtException e) {
            //token hết hạn
            log.trace(INVALID_JWT_TOKEN, e);
        } catch (UnsupportedJwtException e) {
            //Báo hiệu token sử dụng tính năng hoặc thuật toán không được hỗ trợ bởi thư viện JJWT hoặc cấu hình của JwtParser.
            log.trace(INVALID_JWT_TOKEN, e);
        } catch (MalformedJwtException e) {
            //token không đúng định dạng JWT(HEADER.PAYLOAD.SIGNATURE)
            log.trace(INVALID_JWT_TOKEN, e);
        } catch (SignatureException e) {
            //chữ ký không hợp lệ
            log.trace(INVALID_JWT_TOKEN, e);
        } catch (IllegalArgumentException e) {
            //Tham số không hợp lệ
            log.error("Token validation error {}", e.getMessage());
        }
        return false;
    }
}
