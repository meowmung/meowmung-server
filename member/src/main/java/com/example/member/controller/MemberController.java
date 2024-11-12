package com.example.member.controller;

import com.example.member.domain.OauthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MemberController {

    private final RestTemplate restTemplate;

    @Value("${security.oauth2.client.registration.kakao.client-id}")
    private String client_id;
    @Value("${security.oauth2.client.registration.kakao.client-secret}")
    private String client_secret;

    @Value("${security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;

    @Value("${security.oauth2.client.registration.kakao.response-type}")
    private String response_type;

    @GetMapping("oauth/kakao")
    public ResponseEntity<String> login() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("client_id", client_id);
            params.set("redirect_uri", redirect_uri);
        params.set("response_type", response_type);

        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                    .queryParams(params)
                    .encode().build().toUri();
        ResponseEntity<String> b = ResponseEntity.status(302).location(uri).build();
//        var a = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        return b;
    }

    @GetMapping("oauth")
    public String getMember(@RequestParam("code") String code) {
        //header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id); // 여기에 앱 REST API 키를 넣어주세요
        body.add("client_secret", client_secret); // 여기에 앱 REST API 키를 넣어주세요
        body.add("redirect_uri", redirect_uri);  // 여기에 리다이렉트 URI를 넣어주세요
        body.add("code", code);    // 여기에 인가 코드를 넣어주세요

        // 요청 엔터티 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // 요청을 보낼 URLQD
        String url = "https://kauth.kakao.com/oauth/token"; // 여기에 요청을 보낼 URL을 넣어주세요

        // POST 요청 보내기
        String response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class).getBody();

        // 응답 출력
        System.out.println("Response: " + response);
        kakaoProfile(response);
        return response;
    }

    public void kakaoProfile(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response, OauthToken.class);
            System.out.println("---------------------------------------");
            System.out.println(oauthToken);
            System.out.println("---------------------------------------");
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        headers.set("Authorization","Bearer "+oauthToken.getAccess_token());
        System.out.println("Bearer "+oauthToken.getAccess_token());


        // 요청 엔터티 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        // 요청을 보낼 URLQD
        String url = "https://kapi.kakao.com/v2/user/me"; // 여기에 요청을 보낼 URL을 넣어주세요

        // POST 요청 보내기
        String userInfo = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        System.out.println(userInfo);
    }

    @GetMapping("hi")
    public String hi() {
        System.out.println("--------------------------------------");
        System.out.println("Get-hi");
        return "Get - hi";
    }

    @PostMapping("hi")
    public String hi2() {
        System.out.println("--------------------------------------");
        System.out.println("Post-hi");
        return "Post - hi";
    }
}