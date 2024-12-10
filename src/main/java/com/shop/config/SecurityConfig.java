package com.shop.config;


import com.shop.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 📘 보안 설정 클래스임을 나타냄
@EnableWebSecurity
public class SecurityConfig {

    // spring boot 3에서는 필요없음
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
       http
               .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
               .formLogin(formLogin -> formLogin
                       .loginPage("/members/login") // 📘 사용자 정의 로그인 페이지
                       .defaultSuccessUrl("/") // 📘 로그인 성공 후 이동할 URL
                       .usernameParameter("email") // 📘 로그인 폼의 username 입력 필드를 email로 지정
                       .failureUrl("/members/login/error") // 📘 로그인 실패 시 이동할 URL
               )
               .logout(logout -> logout
                       .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 📘 로그아웃 URL
                       .logoutSuccessUrl("/") // 📘 로그아웃 성공 후 이동할 URL
               );

        return http.build();
    }

    /**
     * 📘 비밀번호 암호화를 위한 PasswordEncoder 빈 등록
     *
     * 회원가입 시 비밀번호를 해시화하고, 로그인 시 비밀번호 검증에 사용됩니다.
     * BCryptPasswordEncoder는 보안성이 높은 단방향 해시 알고리즘을 제공합니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.userDetailsService(memberService)
//                .passwordEncoder(passwordEncoder());
//    }


    /**
     * 📘 AuthenticationManager 빈 등록
     *
     * 로그인 인증을 담당하는 AuthenticationManager를 반환합니다.
     * UserDetailsService와 PasswordEncoder를 자동으로 연결합니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}