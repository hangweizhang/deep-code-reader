package com.code.deepreader.ai.orchestrator.memory;

import java.io.Serializable;
import java.time.Instant;

/**
 * Immutable memory entry representing a single user/agent utterance.
 */
public record MemoryMessage(String sessionId,
                            Role role,
                            String content,
                            Instant timestamp) implements Serializable {

    public enum Role {
        USER,
        ASSISTANT
    }
}

