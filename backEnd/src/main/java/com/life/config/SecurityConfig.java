package com.life.config;

import com.life.util.JwtAuthenticationFilter;
import com.life.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 🔐 비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            //  CORS 활성화 -- multipart 때문에 따로 추가해줘야함
        	.cors(Customizer.withDefaults())  

            // 🔥 REST API 방식이면 CSRF 끔
            .csrf(csrf -> csrf.disable())

            // 🔥 세션 사용 안 함 (JWT 대비)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔥 기본 로그인 / HTTP Basic 제거
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            .authorizeHttpRequests(auth -> auth

            	    // =========================
            	    // PUBLIC (GET만 허용)
            	    // =========================
            	    .requestMatchers(HttpMethod.GET,
            	            "/api/recipes/**"
            	    ).permitAll()

            	    .requestMatchers(
            	            "/api/member/login",
            	            "/api/member/signup",
            	            "/api/member/send-email",
            	            "/api/member/verify"
            	    ).permitAll()

            	    .requestMatchers("/images/**").permitAll()

            	    // =========================
            	    // PRIVATE (로그인 필요)
            	    // =========================
            	    .requestMatchers(HttpMethod.POST,
            	            "/api/recipes/**",
            	    		"/api/account/**"
            	    ).authenticated()

            	    .requestMatchers(HttpMethod.PUT,
            	            "/api/recipes/**",
            	            "/api/account/**"
            	    ).authenticated()

            	    .requestMatchers(HttpMethod.DELETE,
            	            "/api/recipes/**"
            	    ).authenticated()

            	    // fallback
            	    .anyRequest().authenticated()
            	
              
            )
        
        	.addFilterBefore(
                jwtAuthenticationFilter(jwtUtil),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}