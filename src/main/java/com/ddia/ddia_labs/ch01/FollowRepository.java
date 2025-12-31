package com.ddia.ddia_labs.ch01;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    //이 사람을 팔로우하는 모든 팔로워들을 찾아
    List<Follow> findByFolloweeId(Long followeeId);

}
