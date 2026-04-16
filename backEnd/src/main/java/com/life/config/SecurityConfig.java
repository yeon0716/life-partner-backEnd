package com.life.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 🔐 비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // 🔥 REST API 방식이면 CSRF 끔
            .csrf(csrf -> csrf.disable())

            // 🔥 세션 사용 안 함 (JWT 대비)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔥 기본 로그인 / HTTP Basic 제거
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // 🔐 URL 권한 설정
            .authorizeHttpRequests(auth -> auth

                // ✅ 누구나 접근 가능 (로그인/회원가입/이메일 인증)
                .requestMatchers(
                    "/api/member/login",
                    "/api/member/signup",
                    "/api/member/send-email",
                    "/api/member/verify"
                ).permitAll()

                // 🔒 관리자 전용 (나중에 ROLE 붙이면 사용)
                .requestMatchers("/api/member/admin/**").hasRole("ADMIN")

                // 🔒 나머지는 로그인 필요
                .anyRequest().authenticated()
            );

        return http.build();
    }
}