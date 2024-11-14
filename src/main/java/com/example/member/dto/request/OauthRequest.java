package com.example.member.dto.request;

import com.example.member.common.LoginType;
import lombok.Getter;

@Getter
public record OauthRequest(String code, LoginType type) {
}
