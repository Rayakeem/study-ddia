package com.ddia.ddia_labs.ch01;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tweets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tweet {

    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;

    public Tweet(Long userId, String content) {
        this.userId = userId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

}
