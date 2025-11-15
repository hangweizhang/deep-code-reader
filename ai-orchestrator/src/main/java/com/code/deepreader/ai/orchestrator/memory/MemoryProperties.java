package com.code.deepreader.ai.orchestrator.memory;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for conversation memory.
 */
@ConfigurationProperties(prefix = "orchestrator.memory")
public class MemoryProperties {

    /**
     * Store type: local or redis.
     */
    private String type = "local";

    /**
     * Number of messages to retain per session.
     */
    private int maxMessages = 20;

    private final Redis redis = new Redis();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaxMessages() {
        return maxMessages;
    }

    public void setMaxMessages(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    public Redis getRedis() {
        return redis;
    }

    public static class Redis {

        /**
         * Redis key prefix.
         */
        private String keyPrefix = "cdr:ai:memory:";

        /**
         * TTL for session entries.
         */
        private Duration ttl = Duration.ofHours(12);

        public String getKeyPrefix() {
            return keyPrefix;
        }

        public void setKeyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
        }

        public Duration getTtl() {
            return ttl;
        }

        public void setTtl(Duration ttl) {
            this.ttl = ttl;
        }
    }
}

