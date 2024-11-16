package com.example.member.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;

@Configuration
public class Config {

    @Value("${spring.mail.host}")
    private String mail_host;
    @Value("${spring.mail.port}")
    private int mail_port;
    @Value("${spring.mail.username}")
    private String mail_username;
    @Value("${spring.mail.password}")
    private String mail_password;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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

    @Value("${spring.data.redis.host}")
    private String redis_host;
    @Value("${spring.data.redis.port}")
    private int redis_port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redis_host, redis_port);
    }

    @Bean
    public Context context() {
        return new Context();
    }
}
