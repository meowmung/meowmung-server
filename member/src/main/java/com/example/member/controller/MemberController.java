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
public class MemberController {
    private final RestTemplate restTemplate;
    @Value("${registration.naver.client-id}")
    private String clientId;
    @Value("${registration.naver.client-secret}")
    private String clientSecret;
    @Value("${registration.naver.authorization-grant-type}")
    private String authorizationGrantType;
    @Value("${registration.naver.redirect-uri}")
    private String redirectUri;
    @Value("${provider.naver.authorization-uri}")
    private String authorizationUri;
    @Value("${provider.naver.token-uri}")
    private String tokenUri;
    @Value("${provider.naver.user-info-uri}")
    private String userInfoUri;

    // 로그인 한다고 하면
    // 네이버 로그인 페이지 ( redirect url ) 을 사용자에게 주기
    // 거기로 사용자가 로그인 하면
    // 인가코드 발급 해줌 ( callback url ) 로
    @GetMapping("login/naver")
    public ResponseEntity<String> naverLogin() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("response_type", "code");
        params.set("client_id", clientId);
        params.set("redirect_uri", redirectUri);
        params.set("state", "STATE_STRING");

        URI uri = UriComponentsBuilder
                .fromUriString(authorizationUri)
                .queryParams(params)
                .encode().build().toUri();
        return ResponseEntity.status(302).location(uri).build();
    }

    // 발급된 인가 코드를 callback url 로 받아야함
    // 그 코드를 가지고 네이버에 token 발급 요청
    @GetMapping("/login/naver/callback")
    public String callback(@RequestParam("code") String code
                        , @RequestParam("state") String state) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", authorizationGrantType);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("state", state);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        String response = restTemplate.exchange(tokenUri, HttpMethod.POST, requestEntity, String.class).getBody();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map jsonMap = objectMapper.readValue(response, Map.class);
            String accessToken = (String) jsonMap.get("access_token");

            System.out.println("Access Token: " + accessToken);
            naverProfile(accessToken);
            return accessToken;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 네이버의 회원 정보를 받기
    public Map<String ,String> naverProfile(String accessToken) {
        String header = "Bearer " + accessToken;

        Map<String ,String> headerMap = new HashMap<>();
        headerMap.put("Authorization", header);

        String profileResponse = getProfile(URI.create(userInfoUri), headerMap);
        Map<String, String> profileMap = new HashMap<>();
        profileMap.put("profile", profileResponse);  // 필요에 따라 프로필 정보를 파싱하여 반환
        return profileMap;
    }

    public String getProfile(URI uri, Map<String ,String> headerMap) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // GET 요청을 보낼 때 HttpEntity로 헤더를 포함시킴
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            System.out.println("Profile response: " + response.getBody());
            return response.getBody(); // 프로필 데이터를 반환
        } catch (Exception e) {
            System.out.println("Error fetching profile: " + e.getMessage());
            return null;
        }
    }

}
