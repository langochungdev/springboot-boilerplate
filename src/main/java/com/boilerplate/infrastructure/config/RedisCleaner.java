package com.boilerplate.infrastructure.config;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCleaner {

    private final RedisConnectionFactory redisConnectionFactory;

    @PreDestroy
    public void clearRedis() {
        try (var connection = redisConnectionFactory.getConnection()) {
            connection.serverCommands().flushDb();
        }
    }
}
