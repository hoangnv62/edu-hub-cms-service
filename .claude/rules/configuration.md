---
paths:
  - "src/main/resources/application.yaml"
  - "src/main/java/vn/edu_hub/service/config/**/*.java"
---

# Application Configuration

Config file: `src/main/resources/application.yaml`

- Database: MySQL on port 3306, Hibernate ddl-auto=update, batch_size=500
- Kafka: bootstrap server on port 9094
- JWT: 24h token validity, 30d for remember-me
- File upload: max 50MB via MinIO object storage (`MinIOService`)
- CORS: allows localhost:3000
- AI: Spring AI with OpenAI-compatible endpoint (Gemini via completions path)
- Server: port 8080, context path `/edu-math`