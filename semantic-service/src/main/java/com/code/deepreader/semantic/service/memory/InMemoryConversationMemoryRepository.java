package com.code.deepreader.semantic.service.memory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(ConversationMemoryRepository.class)
public class InMemoryConversationMemoryRepository implements ConversationMemoryRepository {

    private final Map<String, Deque<String>> memoryStore = new ConcurrentHashMap<>();
    private final int defaultLimit = 20;

    @Override
    public void append(String sessionId, String message) {
        Deque<String> deque = memoryStore.computeIfAbsent(sessionId, key -> new ArrayDeque<>());
        deque.addLast(message);
        while (deque.size() > defaultLimit) {
            deque.removeFirst();
        }
    }

    @Override
    public List<String> loadRecent(String sessionId, int limit) {
        Deque<String> deque = memoryStore.getOrDefault(sessionId, new ArrayDeque<>());
        return new ArrayList<>(deque).subList(Math.max(0, deque.size() - limit), deque.size());
    }

    @Override
    public void truncate(String sessionId) {
        memoryStore.remove(sessionId);
    }
}
