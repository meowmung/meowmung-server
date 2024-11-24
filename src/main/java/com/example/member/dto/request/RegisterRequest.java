package com.example.member.dto.request;

import com.example.member.common.LoginType;
import com.example.member.common.MemberRole;
import com.example.member.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

public record RegisterRequest(String mail, String password, String nickname) {
    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(mail)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .loginType(LoginType.JWT)
                .memberRole(MemberRole.MEMBER)
                .build();
    }
}
