package com.example.member.service;

import com.example.member.common.LoginType;
import com.example.member.dto.OauthTokenDto;
import com.example.member.dto.request.OauthRequest;
import com.example.member.entity.Member;
import com.example.member.oauth.OauthLoginInfo;
import com.example.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import javax.security.auth.login.AccountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final List<OauthLoginInfo> oAuth2LoginInfoList;
    private final MemberRepository memberRepository;

    private OauthLoginInfo findOAuth2LoginType(LoginType type) {
        return oAuth2LoginInfoList.stream()
                .filter(x -> x.type() == type)
                .findFirst()
                .get();
    }

    /**
     * 계쩡이 존재하면 true
     * 없으면 false
     */
    private Boolean emailExists(Member member) {
        Member byEmail = memberRepository.findByEmail(member.getEmail());
        return byEmail != null;
    }

    public String oauthLogin(OauthRequest request, HttpServletResponse response) {
        /**
         * 소셜 로그인 해서 정보 받기
         * 받아서 db에 있으면 통과
         *  없으면 db에 저장 통과
         *  토큰발급
         */
        OauthLoginInfo oauthLoginInfo = findOAuth2LoginType(request.getType());
        ResponseEntity<String> accessTokenRes = oauthLoginInfo.requestAccessToken(request.getCode());
        OauthTokenDto accessTokenDto = oauthLoginInfo.getAccessToken(accessTokenRes);
        ResponseEntity<String> stringResponseEntity = oauthLoginInfo.requestUserInfo(accessTokenDto);
        Member userInfo = oauthLoginInfo.getUserInfo(stringResponseEntity);
        // 수정 필요
        if (!emailExists(userInfo)){
            // 없으면 만들어
            memberRepository.save(userInfo);
        }
        // 있으면 패스
        // return 타입
        return "hi";
    }
}
