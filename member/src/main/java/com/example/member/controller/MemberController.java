package com.example.member.controller;

import com.example.member.domain.OauthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
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

    // 로그인 한다고 하면
    // 네이버 로그인 페이지 ( redirect url ) 을 사용자에게 주기
    // 거기로 사용자가 로그인 하면
    // 인가코드 발급 해줌 ( callback url ) 로
    @GetMapping("login/naver")
    public ResponseEntity<String> naverLogin() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("client_id", client_id);
            params.set("redirect_uri", redirect_uri);
        params.set("response_type", response_type);
        params.set("response_type", "code");
        params.set("client_id", clientId);
        params.set("redirect_uri", redirectUri);
        params.set("state", "STATE_STRING");

        URI uri = UriComponentsBuilder
                .fromUriString(authorizationUri)
                .queryParams(params)
                .encode().build().toUri();
        return ResponseEntity.status(302).location(uri).build();
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                    .queryParams(params)
                    .encode().build().toUri();
        ResponseEntity<String> b = ResponseEntity.status(302).location(uri).build();
//        var a = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        return b;
    }

    // 발급된 인가 코드를 callback url 로 받아야함
    // 그 코드를 가지고 네이버에 token 발급 요청
    @GetMapping("/login/naver/callback")
    public String callback(@RequestParam("code") String code
                        , @RequestParam("state") String state) {

    @GetMapping("oauth")
    public String getMember(@RequestParam("code") String code) {
        //header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id); // 여기에 앱 REST API 키를 넣어주세요
        body.add("client_secret", client_secret); // 여기에 앱 REST API 키를 넣어주세요
        body.add("redirect_uri", redirect_uri);  // 여기에 리다이렉트 URI를 넣어주세요
        body.add("code", code);    // 여기에 인가 코드를 넣어주세요

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", authorizationGrantType);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("state", state);
        // 요청 엔터티 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // 요청을 보낼 URLQD
        String url = "https://kauth.kakao.com/oauth/token"; // 여기에 요청을 보낼 URL을 넣어주세요
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        String response = restTemplate.exchange(tokenUri, HttpMethod.POST, requestEntity, String.class).getBody();

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
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        headers.set("Authorization","Bearer "+oauthToken.getAccess_token());
        System.out.println("Bearer "+oauthToken.getAccess_token());


        // 요청 엔터티 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }

        // 요청을 보낼 URLQD
        String url = "https://kapi.kakao.com/v2/user/me"; // 여기에 요청을 보낼 URL을 넣어주세요
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // POST 요청 보내기
        String userInfo = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        System.out.println(userInfo);
    }

    @GetMapping("hi")
    public String hi() {
        System.out.println("--------------------------------------");
        System.out.println("Get-hi");
        return "Get - hi";
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

    @PostMapping("hi")
    public String hi2() {
        System.out.println("--------------------------------------");
        System.out.println("Post-hi");
        return "Post - hi";
    }
}