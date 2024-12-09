package com.shop.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

//    private final MemberService memberService;
//
//    public SecurityConfig(MemberService memberService) {
//        this.memberService = memberService;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (선택 사항)
//                .authorizeHttpRequests(auth ->
//                        auth
//                                .requestMatchers("/members/login", "/members/login/error", "/members/logout").permitAll() // 로그인, 에러, 로그아웃은 누구나 접근 가능
//                                .anyRequest().authenticated() // 그 외의 요청은 인증 필요
//                )
//                .formLogin(login ->
//                        login
//                                .loginPage("/members/login") // 커스텀 로그인 페이지
//                                .defaultSuccessUrl("/") // 로그인 성공 후 이동할 URL
//                                .usernameParameter("email") // 사용자명 파라미터 이름 설정
//                                .failureUrl("/members/login/error") // 로그인 실패 시 이동할 URL
//                                .permitAll()
//                )
//                .logout(logout ->
//                        logout
//                                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 URL
//                                .logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 URL
//                                .permitAll()
//                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}