package com.shop.controller;

import com.shop.dto.CartItemDto;
import com.shop.service.CartService;
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

@Controller // Spring MVC 컨트롤러로 등록합니다.
@RequiredArgsConstructor // Lombok 어노테이션으로, final 필드에 대한 생성자를 자동 생성합니다.
public class CartController {

    private final CartService cartService; // 장바구니 비즈니스 로직을 처리하는 CartService 객체 주입

    // 장바구니에 상품을 추가하는 API 엔드포인트
    @PostMapping(value = "/cart") // HTTP POST 요청을 "/cart" 경로로 매핑합니다.
    public @ResponseBody ResponseEntity<?> order(
            @RequestBody @Valid CartItemDto cartItemDto, // 요청 본문으로부터 CartItemDto 객체를 매핑합니다. @Valid로 입력 값 검증을 수행합니다.
            BindingResult bindingResult, // 입력 값 검증 결과를 담는 객체
            Principal principal // 인증된 사용자의 정보를 담은 Principal 객체 (로그인된 사용자의 이메일을 가져오기 위해 사용)
    ) {
        // 입력 값 검증 결과 오류가 있는 경우
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder(); // 에러 메시지를 담을 StringBuilder 생성

            List<FieldError> fieldErrors = bindingResult.getFieldErrors(); // 모든 필드 에러 가져오기

            // 각 필드 에러 메시지를 StringBuilder에 추가
            for (FieldError fieldError : fieldErrors) {
                stringBuilder.append(fieldError.getDefaultMessage()); // 검증 실패 메시지를 추가
            }

            // 에러 메시지를 포함한 400 Bad Request 응답 반환
            return new ResponseEntity<String>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName(); // 로그인된 사용자의 이메일 가져오기
        Long cartItemId; // 생성된 장바구니 아이템 ID를 저장할 변수

        try {
            cartItemId = cartService.addCart(cartItemDto, email); // 장바구니에 상품 추가 로직 호출
        } catch (Exception e) {
            // 예외 발생 시 예외 메시지를 포함하여 400 Bad Request 응답 반환
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // 장바구니 아이템 ID를 포함하여 200 OK 응답 반환
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }
}
