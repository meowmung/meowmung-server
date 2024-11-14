package com.example.member.oauth;

import com.example.member.common.LoginType;
import com.example.member.dto.OauthTokenDto;
import com.example.member.entity.Member;
import org.springframework.http.ResponseEntity;

public interface OauthLoginInfo {

    ResponseEntity<String> requestAccessToken(String code);
    OauthTokenDto getAccessToken(ResponseEntity<String> response);
    ResponseEntity<String> requestUserInfo(OauthTokenDto oauthTokenDto);
    Member getUserInfo(ResponseEntity<String> userInfoRes);

    default LoginType type() {
        if(this instanceof KakaoLoginInfo) {
            return LoginType.KAKAO;
        } else if (this instanceof NaverLoginInfo) {
            return LoginType.NAVER;
        } else {
            return LoginType.JWT;
        }
    }
}
