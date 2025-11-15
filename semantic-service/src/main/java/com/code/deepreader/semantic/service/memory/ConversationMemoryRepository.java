package com.code.deepreader.semantic.service.memory;

import java.util.List;

public interface ConversationMemoryRepository {

    void append(String sessionId, String message);

    List<String> loadRecent(String sessionId, int limit);

    void truncate(String sessionId);
}
