package com.shop.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록 페이지 권한 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN") // 📘 가짜 사용자(admin)와 ROLE_ADMIN 권한을 부여
    public void itemFormTest() throws Exception{

        // 옛날 코드
//        mockMvc.perform(
//                MockMvcTester.MockMvcRequestBuilders.get("/admin/item/new")
//        ).andDo(print())
//                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/admin/item/new") // 📘 /admin/item/new에 GET 요청
                )
                .andDo(print()) // 📘 요청 및 응답 내용을 콘솔에 출력
                .andExpect(status().isOk()); // 📘 HTTP 상태 코드가 200(OK)인지 검증
    }


    @Test
// 📘 JUnit 5의 테스트 메서드임을 나타내는 어노테이션
// - 이 어노테이션이 붙은 메서드는 테스트 실행 시 자동으로 호출됩니다.

    @DisplayName("상품 등록 페이지 일반회원 접근 테스트")
// 📘 JUnit 5의 @DisplayName 어노테이션으로, 테스트의 의미를 명확히 설명하는 주석을 추가합니다.
// - 이 경우, "일반 회원이 상품 등록 페이지에 접근했을 때 권한이 없다는 것을 테스트"합니다.

    @WithMockUser(username = "user", roles = "USER")
// 📘 Spring Security의 가짜 사용자(Mock User)를 생성하는 어노테이션
// - **username="user"**: 가짜 사용자 이름을 **user**로 설정합니다.
// - **roles="USER"**: 가짜 사용자의 권한을 **ROLE_USER**로 설정합니다.
// - 이 어노테이션이 없으면 인증되지 않은 사용자로 간주됩니다.
// - 이 어노테이션으로 **ROLE_USER 권한의 사용자로 로그인한 상태로 테스트**할 수 있습니다.

    public void itemFormNotAdminTest() throws Exception {
        mockMvc.perform(
                        get("/admin/item/new") // 📘 **GET 요청**으로 **/admin/item/new** URL에 접근합니다.
                )
                .andDo(print()) // 📘 **요청과 응답의 모든 정보를 콘솔에 출력**합니다. (요청 URL, 헤더, 상태 코드, 본문 등)
                .andExpect(status().isForbidden()); // 📘 **HTTP 상태 코드가 403 (Forbidden) 인지 검증**합니다.
        // - **403 Forbidden**은 접근 권한이 없는 사용자가 페이지에 접근하려고 할 때 발생하는 상태 코드입니다.
    }


}