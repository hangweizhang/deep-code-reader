package com.code.deepreader.semantic.service.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {

    @Bean
    public PromptTemplate moduleSummaryPrompt() {
        return new PromptTemplate("请总结以下源码片段的职责: {{source}}");
    }

    @Bean
    public AbstractChatMemoryAdvisor chatMemoryAdvisor() {
        return new AbstractChatMemoryAdvisor(new InMemoryChatMemory()) {
        };
    }

    @Bean
    public ChatClient.Builder chatClientBuilder(ChatClient.Builder builder) {
        return builder;
    }
}
