package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
// 📘 스프링 컨테이너를 로드하여 통합 테스트를 실행하는 어노테이션
// - 스프링 애플리케이션의 모든 빈(bean)을 로드하고, 실제 애플리케이션과 동일한 환경에서 테스트를 수행합니다.
// - 일반적으로 컨트롤러, 서비스, 리포지토리 등 모든 계층의 기능을 통합적으로 테스트할 때 사용합니다.

@AutoConfigureMockMvc
// 📘 MockMvc를 자동으로 설정하는 어노테이션
// - **MockMvc 객체를 자동으로 생성**하여, **HTTP 요청 및 응답을 테스트**할 수 있도록 합니다.
// - 실제 서버를 띄우지 않고, **Spring MVC의 동작을 시뮬레이션**할 수 있습니다.
// - 컨트롤러 테스트에서 사용되며, API 요청 및 응답을 검증할 수 있습니다.

@Transactional
// 📘 테스트 메서드가 실행될 때 **트랜잭션을 자동으로 시작하고, 테스트가 끝나면 자동으로 롤백**합니다.
// - 테스트 후에 데이터베이스의 데이터가 원래 상태로 복원되므로, **데이터베이스에 영향을 주지 않고 테스트**할 수 있습니다.
// - 매 테스트마다 새로운 데이터베이스 트랜잭션이 시작되고, 테스트가 끝난 후 자동으로 롤백됩니다.

@TestPropertySource(locations = "classpath:application-test.properties")
// 📘 테스트 실행 시 사용할 **환경 설정 파일의 경로를 명시**하는 어노테이션
// - **application-test.properties** 파일을 불러와서, 해당 설정을 테스트에 적용합니다.
// - 예를 들어, **테스트용 데이터베이스 URL, 계정 정보, 환경 설정** 등을 별도로 정의할 수 있습니다.
// - 이 설정은 **application.properties** 파일보다 **우선 적용**되므로, 테스트 환경에 맞는 설정을 사용할 수 있습니다.

class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 📘 테스트용 회원 생성 메서드
     */
    public Member createMember(String email, String password){

        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("라라 크로프트");
        memberFormDto.setAddress("브리타니아");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);

        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception{
        // 1️⃣ 테스트용 사용자 생성
        String email = "ikari@test.com";
        String password = "ikari";
        this.createMember(email, password);

        // 🔍 첫 번째 시도 (실패 예시)
//        mockMvc.perform(
//                        post("/members/login")
//                                .param("email", email)
//                                .param("password", password)
//                ) // ❌ 너무 일찍 perform()의 괄호가 닫혔음
//                .with(csrf()) // 🔥 CSRF 토큰이 perform() 바깥에 위치
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/"));

        // 🔍 두 번째 시도 (성공 예시 - 의미 있는 코드)
        // mockMvc.perform()의 체이닝이 더 잘 되어 있음
        // with(csrf()) 위치가 올바르며, perform()의 닫는 괄호가 정확하게 위치
//        mockMvc.perform(post("/members/login")
//                .param("email",email)
//                .param("password", password)
//                        .with(csrf())) // 🔥 with(csrf())를 다시 사용해야 함
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/"));

        // ✅ 최종 코드 (성공 사례)
        // 🔍 perform() 메서드의 체이닝이 올바르게 이루어짐
        // post(), param(), with(csrf())가 모두 체이닝 내부에 위치함
        // perform() 메서드의 괄호가 모든 체이닝이 끝난 후에 닫히고, andExpect()가 바깥에 위치함
        mockMvc.perform(
                        post("/members/login")
                                .param("email", email)
                                .param("password", password)
                                .with(csrf()) // 모든 체이닝을 완료한 후에 닫음
                ) // mockMvc.perform()의 닫는 괄호
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        /*체이닝 설명

            mockMvc.perform() 시작
            post() 시작 → param() → param() → with(csrf())
            mockMvc.perform()의 닫는 괄호 위치가 정확합니다.
            andExpect()는 mockMvc.perform()의 바깥에 있어야 합니다.
            mockMvc.perform(
                post("/url")
                    .param()
                    .param()
                    .with(csrf())// 체이닝의 마지막
            )// **perform() 종료**
            .andExpect();
        * */

        // 🔍 추가 시도 (과거에 사용한 방식, 책에 나온 예시)
        // formLogin() 메서드를 통해 로그인 테스트를 구현하는 방식
        // 책에 나온 방식으로 의미가 있어 주석으로 보관 중
//        mockMvc.perform(formLogin(formLogin().userParameter("email")
//                .loginProcessingUrl("/members/login")
//                .user(email).password(password))
//                .andExpect(SecurityMockMvResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception{

        String email = "ikari@test.com";
        String password = "ikari";
        this.createMember(email, password);

        // 옛날책에 쓰여진 로그인 실패 코드
        // mockMvc.perform(formLogin().userParameter("email")
        //        .loginProcessingUrl(/members/login)
        //        .user(email).password("12345"))
        //        .andExpect(SecurityMockMvcResultMatchers.unauthenticated())

        mockMvc.perform(
                post("/members/login")
                        .param("email", email)
                        .param("password", "wrongPassword")
                        .with(csrf())
        )
//                .andExpect(status().isUnauthorized()) //??
                .andExpect(status().is3xxRedirection()) // 3xx 리다이렉션이 발생하는지 확인
                .andExpect(redirectedUrl("/members/login/error")); // 로그인 실패 후 리다이렉트 경로 확인
//                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

}