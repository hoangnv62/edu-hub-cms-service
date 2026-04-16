---
paths:
  - "src/main/java/vn/edu_hub/service/ai/**/*.java"
  - "src/main/java/vn/edu_hub/service/config/AiClientConfig.java"
  - "src/main/java/vn/edu_hub/service/service/TaskGenerationService.java"
---

# AI Integration

`AiProviderFactory` uses a registry pattern mapping `AiProviderType` enum (GEMINI, OPENAI, OLLAMA) to `ITaskAiProvider` implementations.

Currently `GeminiTaskAiProvider` is implemented using Spring AI's `ChatClient`. The `TaskGenerationService` orchestrates AI-powered task generation.

Configuration via `AiClientConfig` which creates `ChatClient` bean from `OpenAiChatModel`.