package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller // Spring MVC의 컨트롤러로 동작하는 클래스임을 명시
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동 생성하는 Lombok 어노테이션
public class OrderController {

    private final OrderService orderService; // 주문 관련 비즈니스 로직을 처리하는 서비스 클래스

    @PostMapping(value = "/order") // HTTP POST 요청을 "/order" 경로로 매핑
    public @ResponseBody ResponseEntity<?> order(
            @RequestBody @Valid OrderDto orderDto, // 요청 본문에서 OrderDto 객체를 매핑하고 유효성 검사
            BindingResult bindingResult, // 유효성 검사 결과를 담는 객체
            Principal principal // 인증된 사용자의 정보를 담고 있는 인터페이스
    ) {
        // 유효성 검사에서 에러가 발생한 경우 처리
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder(); // 에러 메시지를 누적할 StringBuilder 생성

            List<FieldError> fieldErrors = bindingResult.getFieldErrors(); // 모든 필드 에러 가져오기

            for (FieldError fieldError : fieldErrors) {
                stringBuilder.append(fieldError.getDefaultMessage()); // 에러 메시지를 StringBuilder에 추가
            }

            // 에러 메시지를 포함한 BAD_REQUEST 응답 반환
            return new ResponseEntity<String>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
        }

        // 인증된 사용자의 이메일 주소 가져오기
        String email = principal.getName();

        Long orderId; // 주문 ID를 저장할 변수

        try {
            // 주문 생성 로직 호출
            orderId = orderService.order(orderDto, email);
        } catch (Exception e) {
            // 예외 발생 시 예외 메시지를 포함한 BAD_REQUEST 응답 반환
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // 주문 생성 성공 시 주문 ID와 함께 OK 응답 반환
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
