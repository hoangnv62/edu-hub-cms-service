---
paths:
  - "src/main/java/**/*.java"
---

# Code Conventions

- **Dependency injection**: Lombok `@RequiredArgsConstructor` + `@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)` — no `@Autowired`
- **Responses**: Wrap with `CommonResponseDTO` via `ResponseUtils.success()`
- **Error codes**: Use `ApiResponseCode` enum for consistent error responses
- **Exceptions**: Throw `BusinessException` for business rule violations — handled by `GlobalExceptionHandler`
- **Logging**: `@Slf4j`
- **JSON**: `@JsonInclude(JsonInclude.Include.NON_NULL)` on response DTOs