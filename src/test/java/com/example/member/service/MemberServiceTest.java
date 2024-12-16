//package com.example.member.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class MemberServiceTest {
//
//    @Autowired
//    private MemberService memberService;
//
//    @Nested
//    @DisplayName("이메일 검증")
//    class ValidEmailTests {
//
//        @Test
//        @DisplayName("올바른 이메일 주소는 예외를 발생시키지 않음")
//        void shouldNotThrowExceptionForValidEmails() {
//            // 테스트할 유효한 이메일 주소들
//            String[] validEmails = {
//                    "test@example.com",
//                    "user.name@domain.co.uk",
//                    "user_name123@sub.domain.org",
//                    "user+mailbox@domain.com",
//                    "user-name@domain.net"
//            };
//
//            // 각 유효한 이메일 주소에 대해 예외가 발생하지 않아야 함
//            for (String email : validEmails) {
//                assertDoesNotThrow(() -> memberService.validateEmail(email),
//                        "Valid email '" + email + "' threw an exception.");
//            }
//        }
//
//        @Test
//        @DisplayName("잘못된 이메일 주소는 예외를 발생시킴")
//        void shouldThrowExceptionForInvalidEmails() {
//            // 테스트할 유효하지 않은 이메일 주소들
//            String[] invalidEmails = {
//                    "plainaddress",
//                    "missingatsign.com",
//                    "@missingusername.com",
//                    "user@.com",
//                    "user@domain..com",
//                    "user@domain,com",
//                    "user@domain space.com",
//                    "user@domain.c"
//            };
//
//            // 각 유효하지 않은 이메일 주소에 대해 예외가 발생해야 함
//            for (String email : invalidEmails) {
//                Exception exception = assertThrows(RuntimeException.class,
//                        () -> memberService.validateEmail(email),
//                        "Invalid email '" + email + "' did not throw an exception.");
//
//                assertEquals("이메일 형식이 올바르지 않습니다.", exception.getMessage(),
//                        "Error message for email '" + email + "' is incorrect.");
//            }
//        }
//    }
//}
