---
paths:
  - "src/main/java/vn/edu_hub/service/security/**/*.java"
  - "src/main/java/vn/edu_hub/service/config/SecurityConfiguration.java"
---

# Security (JWT-based, stateless)

`JWTFilter` (extends `OncePerRequestFilter`) → extracts Bearer token → `TokenProvider` validates and extracts claims (userId, username, roles) → populates `SecurityContext`.

Configured in `SecurityConfiguration` with CSRF disabled, stateless sessions, and endpoint-level regex matchers.

## Request Authentication Flow

1. Request arrives with JWT in Authorization header
2. `JWTFilter` extracts token, validates, populates `SecurityContext`
3. `SecurityConfiguration` checks endpoint permissions
4. Controller uses `@CurrentUserId` annotation to inject user ID
5. On error, `GlobalExceptionHandler` catches and formats response