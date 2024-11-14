package com.example.member.service;

import com.example.member.common.LoginType;
import com.example.member.dto.OauthTokenDto;
import com.example.member.dto.request.LoginRequest;
import com.example.member.dto.request.OauthRequest;
import com.example.member.dto.request.RegisterRequest;
import com.example.member.entity.Member;
import com.example.member.jwt.JwtUtils;
import com.example.member.oauth.OauthLoginInfo;
import com.example.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final List<OauthLoginInfo> oAuth2LoginInfoList;
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public String login(LoginRequest loginRequest){
//        if (!emailExists(loginRequest.email())){
//            throw new RuntimeException();
//        }
        Optional<Member> loginMember = memberRepository.findByEmail(loginRequest.email());
        if(loginMember.isEmpty()) throw new RuntimeException("로그인 실패");
        Member member = loginMember.get();
        if(!passwordEncoder.matches(
                loginRequest.password(), member.getPassword()))
            throw new RuntimeException("로그인 실패");
        return jwtUtils.generateToken(member.getEmail());
    }

    public void register(RegisterRequest registerRequest) {
        Optional<Member> byEmail = memberRepository.findByEmail(registerRequest.email());
        if(byEmail.isPresent()) throw new RuntimeException("이미 등록된 이메일");
//        Optional<Member> byUsername = memberRepository./**/findByUsername(registerRequest.username());
//        if(byUsername.isPresent()) throw new RuntimeException("이미 등록된 이름");
        Member entity = registerRequest.toEntity(passwordEncoder);
        memberRepository.save(entity);
    }

    private OauthLoginInfo findOAuth2LoginType(LoginType type) {
        return oAuth2LoginInfoList.stream()
                .filter(x -> x.type() == type)
                .findFirst()
                .get();
    }

    /**
     * 계쩡이 존재하면 true 없으면 false
     */
    private Boolean emailExists(String email) {
        Member byEmail = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return byEmail != null;
    }

    public String oauthLogin(OauthRequest request, HttpServletResponse response) {
        /**
         * 소셜 로그인 해서 정보 받기
         * 받아서 db에 있으면 통과
         *  없으면 db에 저장 통과
         *  토큰발급
         */
        OauthLoginInfo oauthLoginInfo = findOAuth2LoginType(request.type());
        ResponseEntity<String> accessTokenRes = oauthLoginInfo.requestAccessToken(request.code());
        OauthTokenDto accessTokenDto = oauthLoginInfo.getAccessToken(accessTokenRes);
        ResponseEntity<String> stringResponseEntity = oauthLoginInfo.requestUserInfo(accessTokenDto);
        Member userInfo = oauthLoginInfo.getUserInfo(stringResponseEntity);
        // 수정 필요
        if (!emailExists(userInfo.getEmail())) {
            // 없으면 만들어
            memberRepository.save(userInfo);
        }
        // 있으면 패스
        // return 타입
        return "hi";
    }

}
