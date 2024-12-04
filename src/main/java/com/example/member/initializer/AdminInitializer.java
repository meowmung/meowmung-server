package com.example.member.initializer;

import com.example.member.common.LoginType;
import com.example.member.common.MemberRole;
import com.example.member.dto.request.RegisterRequest;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public void init() {
        if (memberRepository.findByEmail(adminEmail).isEmpty()) {
            Member member = Member.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .nickname("관리자냥")
                    .loginType(LoginType.JWT)
                    .memberRole(MemberRole.ADMIN)
                    .build();
            memberRepository.save(member);
        }
    }
}
