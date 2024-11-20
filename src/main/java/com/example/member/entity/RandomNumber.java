package com.example.member.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "number" , timeToLive = 60*3) // 3분
public class RandomNumber {
    @Id
    private String email;
    private String randomNumber;

    public RandomNumber(String email, String randomNumber) {
        this.email = email;
        this.randomNumber = randomNumber;
    }
}
