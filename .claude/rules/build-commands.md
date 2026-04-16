---
paths:
  - "pom.xml"
  - "mvnw"
  - "mvnw.cmd"
---

# Build & Run Commands

```bash
mvn clean install              # Build
mvn clean package              # Package
mvn spring-boot:run            # Run (port 8080, context path /edu-math)
mvn test                       # Run all tests
mvn test -Dtest=ClassName              # Run single test class
mvn test -Dtest=ClassName#methodName   # Run single test method
```

## Key Dependencies

Spring Boot 3.5.13, Spring Cloud 2025.1.1, Spring AI 1.0.0, JHipster Framework 8.7.2, JJWT 0.12.6, OpenFeign, Problem-spring-web 0.28.0, Lombok, MySQL Connector