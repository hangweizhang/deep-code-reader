package com.code.deepreader.ai.orchestrator.memory;

import java.util.List;

/**
 * Abstraction for storing and retrieving conversational memory.
 */
public interface ConversationMemoryService {

    /**
     * Append a message to a session transcript.
     *
     * @param sessionId session key
     * @param role      speaker role
     * @param content   message content
     */
    void append(String sessionId, MemoryMessage.Role role, String content);

    /**
     * Load the most recent {@code limit} messages.
     *
     * @param sessionId session key
     * @param limit     maximum number of entries
     * @return ordered list from oldest to newest
     */
    List<MemoryMessage> loadRecent(String sessionId, int limit);

    /**
     * Remove all messages associated with session.
     *
     * @param sessionId session key
     */
    void clear(String sessionId);
}

