package com.code.deepreader.ai.orchestrator.memory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Redis-backed memory store for multi-instance deployments.
 */
@Service
@ConditionalOnProperty(prefix = "orchestrator.memory", name = "type", havingValue = "redis")
public class RedisConversationMemoryService implements ConversationMemoryService {

    private static final Logger log = LoggerFactory.getLogger(RedisConversationMemoryService.class);

    private final StringRedisTemplate redisTemplate;
    private final MemoryProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RedisConversationMemoryService(StringRedisTemplate redisTemplate, MemoryProperties properties) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
    }

    @Override
    public void append(String sessionId, MemoryMessage.Role role, String content) {
        MemoryMessage message = new MemoryMessage(sessionId, role, content, Instant.now());
        String key = buildKey(sessionId);
        try {
            redisTemplate.opsForList().rightPush(key, objectMapper.writeValueAsString(message));
            redisTemplate.opsForList().trim(key, -properties.getMaxMessages(), -1);
            redisTemplate.expire(key, properties.getRedis().getTtl());
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize memory message", e);
        }
    }

    @Override
    public List<MemoryMessage> loadRecent(String sessionId, int limit) {
        String key = buildKey(sessionId);
        List<String> values = redisTemplate.opsForList().range(key, -limit, -1);
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        return values.stream().map(this::deserialize).filter(msg -> msg != null).collect(Collectors.toList());
    }

    @Override
    public void clear(String sessionId) {
        redisTemplate.delete(buildKey(sessionId));
    }

    private String buildKey(String sessionId) {
        return properties.getRedis().getKeyPrefix() + sessionId;
    }

    private MemoryMessage deserialize(String value) {
        try {
            return objectMapper.readValue(value, MemoryMessage.class);
        } catch (JsonProcessingException e) {
            log.warn("Failed to deserialize memory message", e);
            return null;
        }
    }
}

