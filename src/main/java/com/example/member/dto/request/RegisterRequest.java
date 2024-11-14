package com.example.member.dto.request;

import com.example.member.common.LoginType;
import com.example.member.common.MemberRole;
import com.example.member.entity.Member;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public record RegisterRequest(String email, String password, String nickname) {
    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .loginType(LoginType.JWT)
                .memberRole(MemberRole.MEMBER)
                .build();
    }
}
