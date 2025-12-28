package com.ddia.ddia_labs.ch01;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("첫 번째는 DB, 두 번째는 Redis에서 가져옴")
    void cacheLogicTest() {
        //given : db에 유저 미리 저장
        User newUser = new User("sohee", "CTO");
        userRepository.save(newUser);
        Long id = newUser.getId();

        //when
        //1. 첫 번재 조회 (레디스 없음 -> 디비에서 가져옴)
        System.out.println("1차 조회 시작");
        User user1 = userService.getUser(id);
        System.out.println("-----------");

        //2. 두 번째 조회 (레디스에 있음 -> 디비 조회 안 하고 레디스에서 가져옴)
        System.out.println("2차 조회 시작");
        User user2 = userService.getUser(id);
        System.out.println("------------");

        //then : 검증
        assertThat(user1.getName()).isEqualTo("sohee");
        assertThat(user2.getName()).isEqualTo("sohee");
    }

}
