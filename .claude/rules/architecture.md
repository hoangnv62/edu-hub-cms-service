# Layered Architecture

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