---
paths:
  - "src/main/java/vn/edu_hub/service/config/Kafka*.java"
  - "src/main/java/vn/edu_hub/service/constants/KafkaConfigConstants.java"
---

# Kafka Message Queue

- **Producer**: JSON serialization, idempotent, acks=all, 5 retries
- **Consumer**: JSON deserialization, auto-offset "latest", batch up to 500 messages, 10s heartbeat, 50s session timeout