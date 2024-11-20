package com.example.member.dto.request;

import lombok.Getter;

@Getter
public class MailCheckRequest {
    private String mail;
    private String code;
}
