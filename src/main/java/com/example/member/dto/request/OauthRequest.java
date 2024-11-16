package com.example.member.dto.request;

import com.example.member.common.LoginType;

public record OauthRequest(String code, LoginType type) {
}
