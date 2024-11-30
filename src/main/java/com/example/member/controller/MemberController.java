package com.example.member.controller;

import com.example.member.dto.request.LoginRequest;
import com.example.member.dto.request.MailCheckRequest;
import com.example.member.dto.request.MailRequest;
import com.example.member.dto.request.OauthRequest;
import com.example.member.dto.request.RegisterRequest;
import com.example.member.entity.Member;
import com.example.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/oauth/login")
    public String login(@RequestBody OauthRequest request, HttpServletResponse response) {
        return memberService.oauthLogin(request, response);
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return memberService.login(loginRequest);
    }

    @PostMapping("/auth/register")
    public Member register(@RequestBody RegisterRequest registerRequest) {
        log.info("register request: {}", registerRequest);
        return memberService.register(registerRequest);
    }

    @PostMapping("/auth/mail")
    public void sendMail(@RequestBody MailRequest mailRequest) {
        memberService.sendMail(mailRequest);
    }

    @GetMapping("/auth/mail")
    public boolean mailCheck(@RequestBody MailCheckRequest mailCheckRequest){
        return memberService.mailCheck(mailCheckRequest);
    }
}
