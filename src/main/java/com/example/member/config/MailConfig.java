package com.example.member.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String mail_host;
    @Value("${spring.mail.port}")
    private int mail_port;
    @Value("${spring.mail.username}")
    private String mail_username;
    @Value("${spring.mail.password}")
    private String mail_password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mail_host);
        javaMailSender.setPort(mail_port);
        javaMailSender.setUsername(mail_username);
        javaMailSender.setPassword(mail_password);

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true"); // STARTTLS 활성화
        props.put("mail.smtp.auth", "true"); // SMTP 인증 활성화
        return javaMailSender;
    }
}
