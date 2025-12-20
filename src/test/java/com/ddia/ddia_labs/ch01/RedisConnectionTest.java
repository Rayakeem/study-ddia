package com.ddia.ddia_labs.ch01;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import java.sql.SQLOutput;

@SpringBootTest
class RedisConnectionTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("redis에 데이터를 저장하고 조회한다")
    void redisTest() {
        //given
        String key = "test:user:1";
        String value = "Stu CTO";

        //when 저장하고 꺼내기
        redisTemplate.opsForValue().set(key,value);

        Object result = redisTemplate.opsForValue().get(key);

        //then 검증
        System.out.println("가져온 값: " + result);
        assertThat(result).isEqualTo(value);

    }
}
