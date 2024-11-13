package com.example.member.dto.request;

import com.example.member.common.LoginType;
import lombok.Getter;

@Getter
public class OauthRequest {
    private String code;

    private LoginType type;
}
