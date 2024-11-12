package com.example.member.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OauthToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String scope;
    private String expires_in;  // 추가된 필드
    private int refresh_token_expires_in;
}
