package com.ddia.ddia_labs.ch01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@SpringBootTest
public class FeedServiceTest {

    @Autowired
    private FeedService feedService;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("유저가 글을 쓰면 팔로워의 타임라인(레디스)에 트윗 id가 들어간다. fan-out")
    void fanOutWriteTest() {

        //given 상황설정
        Long authorId = 100L;
        Long followerId = 200L;

        followRepository.saveAndFlush(new Follow(followerId, authorId));

        //when 햏동
        //100번 유저가 안녕하세요! 라고 트윗을 씀
        feedService.postTweet(authorId, "안녕하세요! DDIA 실습중입니다~");

        //then 검증
        // 200번 유저(팬)의 타임라인 키: "timeline:200"
        // 여기에 방금 쓴 트윗의 ID가 들어와 있어야 함!

        // Redis List에서 데이터 꺼내보기 (0번부터 -1번까지 = 전체 조회)
        List<Object> timeline = redisTemplate.opsForList().range("timeline:" + followerId, 0, -1);

        System.out.println("팬(200번)의 타임라인: " + timeline);

        // 타임라인이 비어있으면 안 됨 (Fan-out이 동작했다면!)
        assertThat(timeline).isNotEmpty();
    }

    @Test
    @DisplayName("피드 조회: 내 우편함(Redis)을 열면 팔로우한 사람의 글이 보여야 한다")
    void readFeedTest() {
        //given
        Long celebrityId = 900L;
        Long fanId = 700L;

        //팔로우 설정
        followRepository.saveAndFlush(new Follow(fanId, celebrityId));

        //when
        //팔로이가 글 씀
        feedService.postTweet(celebrityId, "오늘 엽떡 먹고 혈스 와서 10시간 자고 싶다!");

        // 팬이 "내 피드 보여줘!" 하고 요청함
        List<Tweet> myFeed = feedService.getFeed(fanId);

        //then
        // 1. 피드가 비어있으면 안 됨
        assertThat(myFeed).isNotEmpty();
        // 2. 그 글의 내용이 "오늘 날씨 참 좋네요!" 여야 함
        assertThat(myFeed.get(0).getContent()).isEqualTo("오늘 엽떡 먹고 혈스 와서 10시간 자고 싶다!");

    }
}
