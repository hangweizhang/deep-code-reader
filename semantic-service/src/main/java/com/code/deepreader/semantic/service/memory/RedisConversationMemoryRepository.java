package com.code.deepreader.semantic.service.memory;

import java.time.Duration;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "platform.memory", name = "type", havingValue = "redis")
public class RedisConversationMemoryRepository implements ConversationMemoryRepository {

    private final StringRedisTemplate redisTemplate;

    public RedisConversationMemoryRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void append(String sessionId, String message) {
        String key = buildKey(sessionId);
        redisTemplate.opsForList().rightPush(key, message);
        redisTemplate.opsForList().trim(key, -50, -1);
        redisTemplate.expire(key, Duration.ofHours(12));
    }

    @Override
    public List<String> loadRecent(String sessionId, int limit) {
        String key = buildKey(sessionId);
        return redisTemplate.opsForList().range(key, -limit, -1);
    }

    @Override
    public void truncate(String sessionId) {
        redisTemplate.delete(buildKey(sessionId));
    }

    private String buildKey(String sessionId) {
        return "cdr:conversation:" + sessionId;
    }
}
