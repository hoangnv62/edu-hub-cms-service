package vn.edu_hub.service.ai.factory;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import vn.edu_hub.service.ai.provider.ITaskAiProvider;
import vn.edu_hub.service.constants.AiProviderType;
import vn.edu_hub.service.constants.ApiResponseCode;
import vn.edu_hub.service.exception.BusinessException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AiProviderFactory {
    Map<AiProviderType, ITaskAiProvider> providers;

    public AiProviderFactory(List<ITaskAiProvider> providers) {
        this.providers = providers.stream()
                .collect(Collectors.toMap(
                        ITaskAiProvider::getAiProviderType,
                        Function.identity()));
    }

    public ITaskAiProvider getProvider(AiProviderType providerType) {
        if (providerType == null) {
            throw new BusinessException(ApiResponseCode.INTERNAL_SERVER_ERROR, "providerType is null");
        }
        ITaskAiProvider provider = providers.get(providerType);
        if (provider == null) {
            throw new BusinessException(ApiResponseCode.INTERNAL_SERVER_ERROR, "provider is null");
        }
        return provider;
    }
}
