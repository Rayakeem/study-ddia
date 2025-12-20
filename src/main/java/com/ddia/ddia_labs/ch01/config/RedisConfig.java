package com.ddia.ddia_labs.ch01.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key는 String으로 저장 (예: "user:1")
        template.setKeySerializer(new StringRedisSerializer());

        // Value는 JSON으로 저장 (예: {"name": "stu", "role": "CTO"})
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
