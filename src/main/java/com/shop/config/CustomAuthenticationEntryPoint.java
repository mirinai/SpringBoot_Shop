package com.shop.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
/**
 * CustomAuthenticationEntryPoint 클래스는 인증되지 않은 사용자가 리소스에 접근할 때 호출됩니다.
 * Spring Security의 AuthenticationEntryPoint 인터페이스를 구현하여
 * 인증 예외(AuthenticationException)가 발생했을 때의 동작을 정의합니다.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출됩니다.
     *
     * @param request  HttpServletRequest - 사용자의 요청 정보가 담긴 객체
     * @param response HttpServletResponse - 사용자에게 응답을 보내기 위한 객체
     * @param authException AuthenticationException - 인증 예외 정보가 담긴 객체
     *
     * @throws IOException - 입출력 예외가 발생할 수 있습니다.
     * @throws ServletException - 서블릿 관련 예외가 발생할 수 있습니다.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 요청의 헤더에 "x-requested-with"가 "XMLHttpRequest"로 설정된 경우 (AJAX 요청)
        if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))){
            // AJAX 요청의 경우 401 Unauthorized 상태 코드를 응답으로 보냄
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");


        }
        else{
            // 일반 요청의 경우 로그인 페이지로 리다이렉트
            response.sendRedirect("/members/login");
        }
    }
}
