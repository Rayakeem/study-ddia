package com.ddia.ddia_labs.ch01;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.time.Duration;

@Service
@RequiredArgsConstructor //생성자 주입 자동화
public class UserService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public User getUser(Long id) {
        //redis 키 생성
        String key = "users:" +  id;

        // 1. redis에서 먼저 조회
        User cachedUser= (User) redisTemplate.opsForValue().get(key);
        if(cachedUser != null) {
            System.out.println("cached hit! redis에서 가져옴: " + id);
            return cachedUser;
        }

        // 2. 없으면 db에서 조회
        System.out.println("cached miss + db 조회 중: "+ id);
        User user = userRepository.findById(id)
                .orElseThrow( ()-> new IllegalArgumentException("유저가 없습니다."));

        // 3. db에서 가져온 데이터를 redis에 저장 (다음 조회를 위해)
        //set(key, value, 유효시간) -> 유효시간 뒤에 자동 삭제되도록 TTL setting
        redisTemplate.opsForValue().set(key, user, Duration.ofMinutes(10));

        return user;
    }
}
