package com.example.member.initializer;

import com.example.member.common.LoginType;
import com.example.member.common.MemberRole;
import com.example.member.dto.request.RegisterRequest;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public void init() {
        if (memberRepository.findByEmail("meowmungg@gmail.com").isEmpty()) {
            Member member = Member.builder()
                    .email("meowmungg@gmail.com")
                    .password(passwordEncoder.encode("qwer1234!@#$"))
                    .nickname("관리자냥")
                    .loginType(LoginType.JWT)
                    .memberRole(MemberRole.ADMIN)
                    .build();
            memberRepository.save(member);
        }
    }
}
