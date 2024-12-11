package com.shop.config;


import com.shop.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
//               .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
               .formLogin(formLogin -> formLogin
                       .loginPage("/members/login") // 📘 사용자 정의 로그인 페이지
                       .defaultSuccessUrl("/") // 📘 로그인 성공 후 이동할 URL
                       .usernameParameter("email") // 📘 로그인 폼의 username 입력 필드를 email로 지정
                       .failureUrl("/members/login/error") // 📘 로그인 실패 시 이동할 URL
                       .permitAll() // 📘 로그인 관련 URL에 대해 모든 사용자에게 접근 허용
               )
               .logout(logout -> logout
                       .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 📘 로그아웃 URL
                       .logoutSuccessUrl("/") // 📘 로그아웃 성공 후 이동할 URL
                       .permitAll() // 📘 로그아웃 관련 URL에 대해 모든 사용자에게 접근 허용
               )
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/", "/members/**","/item/**","/images/**").permitAll()// 📘 모든 사용자가 접근 가능
                       .requestMatchers("/admin/**").hasRole("ADMIN")// 📘 ADMIN Role만 접근 가능
                       .anyRequest().authenticated() // 📘 나머지 모든 요청은 인증을 요구
               )
               .exceptionHandling(exceptionHandling ->
                       exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint())

               ) // 📘 인증되지 않은 사용자에 대한 예외 처리
       ;

//       http.authorizeRequests() //시큐리티 처리에 HttpServletRequest를 이용한다는 것을 뜻함
//               .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll() // permitAll()을 통해 모든 유저가 인증(로그인) 없이 해당 경로에 접근 할 수 있도록
//               .mvcMatchers("/admin/**").hasRole("ADMIN") // /ADMIN으로 시작하는 경로는 해당 계정이 ADMIN Role일 경우에만 접근 하도록
//               .anyRequest().authenticated() //나머지 경로들은 모두 인증을 요구하도록 함
//       ;// 옛날코드
//       http.exceptionHandling()
//               .authenticationEntryPoint
//                       (new CustomAuthenticationEntryPoint()) // 인증되지 않은 유저가 리소스에 접근하였을 때 수행되는 핸들러를 등록
//       ;//옛날코드
//
//        @Override
//        public void configure(WebSecurity web) throws Exception{
//            web.ignoring().antMatchers("/css/**", "/js/**","/img/**"); // static 디렉터리의 하위 파일은 인증 무시하기
//        }  // 옛날 코드

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

    /**
     * 📘 정적 리소스에 대한 접근 허용 설정
     * - static 디렉터리의 하위 파일(css, js, img 등)은 보안 필터를 무시하도록 설정합니다.
     * - WebSecurityCustomizer를 통해 필터링되지 않도록 설정합니다.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/img/**");
    }
}