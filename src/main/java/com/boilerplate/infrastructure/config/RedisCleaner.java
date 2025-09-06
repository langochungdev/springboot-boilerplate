package com.boilerplate.infrastructure.config;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCleaner {

    private final RedisConnectionFactory redisConnectionFactory;

    @PreDestroy
    public void clearRedis() {
        if (redisConnectionFactory instanceof LettuceConnectionFactory lettuce && lettuce.isRunning()) {
            try (var connection = redisConnectionFactory.getConnection()) {
                connection.serverCommands().flushDb();
            }
        }
    }
}
