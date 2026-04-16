# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**edu-hub-cms-service** is a Spring Boot 3.5.13 microservice (Java 17, Maven) for managing educational content — classes, lessons, tasks, questions, student submissions. It integrates with AI providers for automated task generation.

## Build & Run Commands

```bash
mvn clean install              # Build
mvn clean package              # Package
mvn spring-boot:run            # Run (port 8080, context path /edu-math)
mvn test                       # Run all tests
mvn test -Dtest=ClassName              # Run single test class
mvn test -Dtest=ClassName#methodName   # Run single test method
```

## Architecture

### Layered Pattern

```
Controller → Service → Repository → Domain (JPA Entity)
     ↕            ↕
   DTO        Exception
(request/     (BusinessException →
 response)    GlobalExceptionHandler)
```

- **Controllers** (`controller/`): `@RestController`, uses `@CurrentUserId` to inject authenticated user ID
- **Services** (`service/`): `@Service` + `@Transactional`, validates input then throws `BusinessException` on violations
- **Repositories** (`repository/`): `JpaRepository` with custom `@Query` JPQL and projection interfaces
- **Domain** (`domain/`): JPA entities inheriting `AbstractAuditingEntity` for automatic audit fields (createdDate, lastModifiedDate, createdBy, lastModifiedBy)
- **DTOs** (`dto/request/`, `dto/response/`): Some use Java records, validated with `@Valid`/`@Validated`

### Security (JWT-based, stateless)

`JWTFilter` (extends `OncePerRequestFilter`) → extracts Bearer token → `TokenProvider` validates and extracts claims (userId, username, roles) → populates `SecurityContext`. Configured in `SecurityConfiguration` with CSRF disabled, stateless sessions, and endpoint-level regex matchers.

### AI Integration

`AiProviderFactory` uses a registry pattern mapping `AiProviderType` enum (GEMINI, OPENAI, OLLAMA) to `ITaskAiProvider` implementations. Currently `GeminiTaskAiProvider` is implemented using Spring AI's `ChatClient`. The `TaskGenerationService` orchestrates AI-powered task generation.

### Kafka

Producer: JSON serialization, idempotent, acks=all, 5 retries.
Consumer: JSON deserialization, auto-offset "latest", batch up to 500 messages.

### File Storage

`MinIOService` handles file uploads/downloads via MinIO object storage.

## Code Conventions

- **Dependency injection**: Lombok `@RequiredArgsConstructor` + `@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)` — no `@Autowired`
- **Responses**: Wrap with `CommonResponseDTO` via `ResponseUtils.success()`
- **Error codes**: Use `ApiResponseCode` enum for consistent error responses
- **Logging**: `@Slf4j`
- **JSON**: `@JsonInclude(JsonInclude.Include.NON_NULL)` on response DTOs

## Configuration

Application config is in `src/main/resources/application.yaml`:
- Database: MySQL on port 3306, Hibernate ddl-auto=update, batch_size=500
- Kafka: bootstrap server on port 9094
- JWT: 24h token validity, 30d for remember-me
- File upload: max 50MB
- CORS: allows localhost:3000
- AI: Spring AI with OpenAI-compatible endpoint (Gemini via completions path)

## Key Dependencies

Spring Boot 3.5.13, Spring Cloud 2025.1.1, Spring AI 1.0.0, JHipster Framework 8.7.2, JJWT 0.12.6, OpenFeign, Problem-spring-web 0.28.0, Lombok, MySQL Connector