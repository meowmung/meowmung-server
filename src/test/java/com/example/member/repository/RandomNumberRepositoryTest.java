//package com.example.member.repository;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.example.member.entity.RandomNumber;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class RandomNumberRepositoryTest {
//    @Autowired
//    private RandomNumberRepository repository;
//
//    @Test
//    @DisplayName("Redis 동작 확인 테스트")
//    void redisTest(){
//        RandomNumber randomNumber = new RandomNumber("hi@toss.com", "123123");
//        repository.save(randomNumber);
//        String redisNumber = repository.findById(randomNumber.getEmail()).get().getRandomNumber();
//        assertEquals(randomNumber.getRandomNumber(), redisNumber);
//    }
//
//}