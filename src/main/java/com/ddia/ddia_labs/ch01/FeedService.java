package com.ddia.ddia_labs.ch01;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class FeedService {

    //의존성 주입받을 것들 미리 선언
    private final TweetRepository tweetRepository;
    private final FollowRepository followRepository;

    //레디스 주입
    private final RedisTemplate<String, Object> redisTemplate;

    public void postTweet(Long userId, String content){

        //디비에 트윗 영구 저장
        Tweet tweet = new Tweet(userId, content);
        tweetRepository.save(tweet);

        //팔로우 목록을 디비에서 가져옴
        List<Follow> followers = followRepository.findByFolloweeId(userId);

        //팔로우들의 레디스에 트윗 넣어줌
        // 3. [Fan-out] 팬들의 타임라인(Redis)에 트윗 ID를 배달
        for (Follow follow : followers) {
            String key = "timeline:" + follow.getFollowerId(); // 예: timeline:200

            // leftPush: 리스트의 가장 왼쪽(맨 앞)에 넣는다. (최신 글이 위로 오게!)
            redisTemplate.opsForList().leftPush(key, tweet.getId());
        }
    }

    // "내 ID(fanId)를 줄 테니까, 내 피드(Tweet 목록)를 줘"
    public List<Tweet> getFeed(Long fanId) {
        // 1. 내 우편함 키 생성
        String key = "timeline:" + fanId;

        // 2. Redis에서 트윗 ID 목록 꺼내기 (0부터 -1까지 = 전체 조회)
        // 주의: RedisTemplate<String, Object>라서 리턴타입이 List<Object>임
        List<Object> tweetIdsObjects = redisTemplate.opsForList().range(key, 0, -1);

        // 3. 우편함이 비어있으면? 빈 리스트 리턴하고 끝
        if (tweetIdsObjects == null || tweetIdsObjects.isEmpty()) {
            return List.of();
        }

        // 4. Object 리스트를 Long 리스트로 변환 (형변환)
        // Redis에서 숫자가 Integer로 나올 수도 있고 Long으로 나올 수도 있어서,
        // 안전하게 String으로 바꿨다가 Long으로 파싱하는 꼼수를 씀
        List<Long> tweetIds = tweetIdsObjects.stream()
                .map(id -> Long.valueOf(id.toString()))
                .toList();

        // 5. DB에서 실제 트윗 내용물(Content)을 한방에 가져오기 (WHERE id IN (...))
        return tweetRepository.findAllById(tweetIds);
    }

}
