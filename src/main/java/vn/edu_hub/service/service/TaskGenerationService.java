package vn.edu_hub.service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.edu_hub.service.ai.factory.AiProviderFactory;
import vn.edu_hub.service.constants.AiProviderType;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskGenerationService {
    AiProviderFactory aiProviderFactory;

    public String generateTask(String prompt, AiProviderType aiProviderType) {
        return aiProviderFactory.getProvider(aiProviderType).generateTask(prompt);
    }
}
