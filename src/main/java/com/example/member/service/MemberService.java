package com.example.member.service;

import com.example.member.common.LoginType;
import com.example.member.dto.OauthTokenDto;
import com.example.member.dto.request.LoginRequest;
import com.example.member.dto.request.MailCheckRequest;
import com.example.member.dto.request.MailRequest;
import com.example.member.dto.request.OauthRequest;
import com.example.member.dto.request.RegisterRequest;
import com.example.member.entity.Member;
import com.example.member.entity.RandomNumber;
import com.example.member.jwt.JwtUtils;
import com.example.member.oauth.OauthLoginInfo;
import com.example.member.repository.MemberRepository;
import com.example.member.repository.RandomNumberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final List<OauthLoginInfo> oAuth2LoginInfoList;
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final Context context;
    private final RandomNumberRepository randomNumberRepository;

    public String login(LoginRequest loginRequest) {
//        if (!emailExists(loginRequest.email())){
//            throw new RuntimeException();
//        }
        validateEmail(loginRequest.mail());
        Optional<Member> loginMember = memberRepository.findByEmail(loginRequest.mail());
        if (loginMember.isEmpty()) {
            throw new RuntimeException("로그인 실패");
        }
        Member member = loginMember.get();
        if (!passwordEncoder.matches(
                loginRequest.password(), member.getPassword())) {
            throw new RuntimeException("로그인 실패");
        }
        return jwtUtils.generateToken(member.getEmail(), member.getNickname(), member.getId());
    }

    public void register(RegisterRequest registerRequest) {
        validateEmail(registerRequest.mail());
        validatePassword(registerRequest.password());

        Optional<Member> byEmail = memberRepository.findByEmail(registerRequest.mail());
        if (byEmail.isPresent()) {
            throw new RuntimeException("이미 등록된 이메일");
        }
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

    public void sendMail(MailRequest mailRequest) {
        validateEmail(mailRequest.getMail());
        String code = createCode();
        context.setVariable("code", code);
        String process = templateEngine.process("mail-template", context);
        RandomNumber randomNumber = new RandomNumber(mailRequest.getMail(), code);
        randomNumberRepository.save(randomNumber);
        // 수신자, 제목, 내용 설정
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setTo(mailRequest.getMail());
            message.setSubject("멍냥냥 요청하신 인증 번호를 알려드립니다.");
            message.setText(process, true);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        // 메일 전송
        javaMailSender.send(mimeMessage);
    }

    public boolean mailCheck(MailCheckRequest mailCheckRequest) {
        validateEmail(mailCheckRequest.getMail());
        Optional<RandomNumber> byId = randomNumberRepository.findById(mailCheckRequest.getMail());
        if (byId.isEmpty()) {
            throw new RuntimeException("그런 이메일 없음");
        }
        if (!Objects.equals(mailCheckRequest.getCode(), byId.get().getRandomNumber())) {
            return false;
        }
        return true;
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
        // 유저 정보 만들었습니다.
        Member savedMember;
        if (!emailExists(userInfo.getEmail())) {
            // 계정이 존재 하지 않으면 만들어서
            savedMember = memberRepository.save(userInfo);
        }
        savedMember = memberRepository.findByEmail(userInfo.getEmail())
                .orElseThrow(RuntimeException::new);
        // 있으면 패스
        return jwtUtils.generateToken(userInfo.getEmail(), userInfo.getNickname(), savedMember.getId());
    }

    public void validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);

        if (!emailMatcher.matches()) {
            throw new RuntimeException("이메일 형식이 올바르지 않습니다.");
        }
    }


    public void validatePassword(String password) {
        // 최소 8자, 소문자 하나 이상, 숫자 하나 이상, 특수 문자 하나 이상 포함
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);

        if (password == null || !pattern.matcher(password).matches()) {
            throw new IllegalArgumentException("비밀번호가 보안 요건을 충족하지 않습니다.");
        }
    }

    public String createCode() {
        int length = 6;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(characters.length());
                builder.append(characters.charAt(index));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("MemberService.createCode() exception occur");
            throw new RuntimeException(e);
        }
    }
}
