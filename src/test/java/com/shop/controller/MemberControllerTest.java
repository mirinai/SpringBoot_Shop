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
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
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