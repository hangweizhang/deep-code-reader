package com.code.deepreader.ai.orchestrator.memory;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * In-memory implementation suitable for local development.
 */
@Service
@ConditionalOnProperty(prefix = "orchestrator.memory", name = "type", havingValue = "local", matchIfMissing = true)
public class LocalConversationMemoryService implements ConversationMemoryService {

    private final Map<String, Deque<MemoryMessage>> memoryStore = new ConcurrentHashMap<>();
    private final MemoryProperties properties;

    public LocalConversationMemoryService(MemoryProperties properties) {
        this.properties = properties;
    }

    @Override
    public void append(String sessionId, MemoryMessage.Role role, String content) {
        Deque<MemoryMessage> deque = memoryStore.computeIfAbsent(sessionId, key -> new ArrayDeque<>());
        deque.addLast(new MemoryMessage(sessionId, role, content, Instant.now()));
        while (deque.size() > properties.getMaxMessages()) {
            deque.removeFirst();
        }
    }

    @Override
    public List<MemoryMessage> loadRecent(String sessionId, int limit) {
        Deque<MemoryMessage> deque = memoryStore.get(sessionId);
        if (deque == null || deque.isEmpty()) {
            return List.of();
        }
        int start = Math.max(0, deque.size() - limit);
        return deque.stream().skip(start).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public void clear(String sessionId) {
        memoryStore.remove(sessionId);
    }
}

