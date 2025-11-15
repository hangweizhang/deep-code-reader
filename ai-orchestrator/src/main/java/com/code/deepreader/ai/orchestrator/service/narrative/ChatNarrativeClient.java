package com.code.deepreader.ai.orchestrator.service.narrative;

import com.code.deepreader.ai.orchestrator.config.LlmModelProperties.ModelConfig;
import com.code.deepreader.ai.orchestrator.service.model.LlmChatClientManager;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class ChatNarrativeClient implements NarrativeClient {

    private final LlmChatClientManager chatClientManager;

    public ChatNarrativeClient(LlmChatClientManager chatClientManager) {
        this.chatClientManager = chatClientManager;
    }

    @Override
    public String generate(String prompt, String modelName) {
        ChatClient client = chatClientManager.resolveClient(modelName);
        return client.prompt()
                .user(prompt)
                .call()
                .content();
    }
}

