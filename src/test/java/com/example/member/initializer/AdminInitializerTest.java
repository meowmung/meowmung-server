//package com.example.member.initializer;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.example.member.entity.Member;
//import com.example.member.repository.MemberRepository;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class AdminInitializerTest {
//    @Autowired
//    private AdminInitializer adminInitializer;
//    @Autowired
//    private MemberRepository memberRepository;
//
//
//    @Test
//    @DisplayName("Admin 생성 검증")
//    void checkAdmin() {
//        adminInitializer.init();
//        Optional<Member> byEmail = memberRepository.findByEmail("meowmungg@gmail.com");
//        if (byEmail.isPresent()) {
//            assertEquals(byEmail.get().getEmail(), "meowmungg@gmail.com", "ADMIN");
//        }
//    }
//}