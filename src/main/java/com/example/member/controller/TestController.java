/*
package com.example.member.controller;

import com.example.member.entity.CustomUserDetails;
import com.example.member.entity.Member;
import com.example.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties.View;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class TestController {

    @Value("${security.oauth2.client.registration.kakao.client-id}")
    private String kakao_client_id;

    @Value("${security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakao_redirect_uri;

    @Value("${security.oauth2.client.registration.naver.client-id}")
    private String naver_client_id;

    @Value("${security.oauth2.client.registration.naver.client-secret}")
    private String naver_client_secret;

    @Value("${security.oauth2.client.registration.naver.redirect-uri}")
    private String naver_redirect_uri;

    private final MemberService memberService;

    // 로그인 한다고 하면
    // 네이버 로그인 페이지 ( redirect url ) 을 사용자에게 주기
    // 거기로 사용자가 로그인 하면
    // 인가코드 발급 해줌 ( callback url ) 로
    @GetMapping("login/kakao")
    public ResponseEntity<String> naverKakao() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("response_type", "code");
        params.set("client_id", kakao_client_id);
        params.set("redirect_uri", kakao_redirect_uri);
        params.set("state", "STATE_STRING");

        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParams(params)
                .encode().build().toUri();
        return ResponseEntity.status(302).location(uri).build();
    }

    @GetMapping("login/naver")
    public ResponseEntity<String> naverLogin() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("response_type", "code");
        params.set("client_id", naver_client_id);
        params.set("redirect_uri", naver_redirect_uri);
        params.set("state", "STATE_STRING");

        URI uri = UriComponentsBuilder
                .fromUriString("https://nid.naver.com/oauth2.0/authorize")
                .queryParams(params)
                .encode().build().toUri();
        return ResponseEntity.status(302).location(uri).build();
    }

    @GetMapping("/test/v1")
    public void test(@AuthenticationPrincipal String hi) {
//        String code = memberService.createCode();
        System.out.println(hi);
//        return mai
    }

    @GetMapping("/test/v2") // 이게 성공
    public void testb2(@AuthenticationPrincipal CustomUserDetails hi) {
//        String code = memberService.createCode();
        System.out.println(hi.getAuthorities());
//        hi.getAuthorities()
        System.out.println(hi.getPassword()); // 업승
        System.out.println(hi.getUsername());
        System.out.println(hi);
//        return mai
    }

    @GetMapping("/test/v3")
    public void testv3(@AuthenticationPrincipal Member hi) {
//        String code = memberService.createCode();
        System.out.println(hi);
//        return mai
    }

    @GetMapping("/test/v4")
    public void testv3(@RequestHeader("X-Authorization-nickname") String hi,
                       @RequestHeader("X-Authorization-email") String hi2) {
//        String code = memberService.createCode();
        System.out.println(hi2);
        System.out.println(hi);
//        return mai
    }
    @GetMapping("/test/v5")
    public void testv5(@RequestHeader("Authorization") String token,
                       @RequestHeader("X-Authorization-memberId") String memberId) {
        System.out.println(token);
        System.out.println(memberId);
//        return mai
    }
}*/
