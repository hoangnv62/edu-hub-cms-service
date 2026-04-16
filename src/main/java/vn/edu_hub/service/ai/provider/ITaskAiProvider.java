package vn.edu_hub.service.ai.provider;

import vn.edu_hub.service.constants.AiProviderType;

public interface ITaskAiProvider {
    String generateTask(String prompt);
    AiProviderType getAiProviderType();
}
