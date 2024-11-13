package com.example.member.oauth;

import com.example.member.dto.OauthTokenDto;
import com.example.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class NaverLoginInfo implements OauthLoginInfo{
//
//    private final RestTemplate restTemplate;
//    @Value("${security.oauth2.client.registration.naver.client-id}")
//    private String naver_client_id;
//    @Value("${security.oauth2.client.registration.naver.client-id}")
//    private String naver_client_secret;
//    @Value("${security.oauth2.client.registration.naver.client-id}")
//    private String naver_authorization_grantType;
//    @Value("${security.oauth2.client.registration.naver.client-id}")
//    private String redirectUri;
//    @Value("${security.oauth2.client.registration.naver.client-id}")
//    private String authorizationUri;
////    @Value("${provider.naver.token-uri}")
////    private String tokenUri;
//    @Value("${provider.naver.user-info-uri}")
    private String userInfoUri;
    @Override
    public ResponseEntity<String> requestAccessToken(String code) {
        return null;
    }

    @Override
    public OauthTokenDto getAccessToken(ResponseEntity<String> response) {
        return null;
    }

    @Override
    public ResponseEntity<String> requestUserInfo(OauthTokenDto oauthTokenDto) {
        return null;
    }

    @Override
    public Member getUserInfo(ResponseEntity<String> userInfoRes) {
        return null;
    }
}
