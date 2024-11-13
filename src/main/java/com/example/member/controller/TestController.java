package com.example.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
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
    // 로그인 한다고 하면
    // 네이버 로그인 페이지 ( redirect url ) 을 사용자에게 주기
    // 거기로 사용자가 로그인 하면
    // 인가코드 발급 해줌 ( callback url ) 로
    @GetMapping("login/kakao")
    public ResponseEntity<String> naverLogin() {
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

}