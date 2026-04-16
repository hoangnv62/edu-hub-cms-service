package vn.edu_hub.service.ai.provider;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import vn.edu_hub.service.constants.AiProviderType;

@Service
public class GeminiTaskAiProvider implements ITaskAiProvider {
    private final ChatClient chatClient;

    public GeminiTaskAiProvider(@Qualifier("openAiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String generateTask(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    @Override
    public AiProviderType getAiProviderType() {
        return AiProviderType.GEMINI;
    }
}
