package com.shop.controller;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/cart") // HTTP GET 요청을 "/cart" 경로로 매핑합니다. 장바구니 페이지를 보여줄 때 사용합니다.
    public String orderHist(Principal principal, Model model) {
        // 로그인된 사용자의 이메일을 통해 장바구니에 담긴 상품 목록을 가져옵니다.
        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal.getName());

        // 뷰에 "cartItems"라는 이름으로 장바구니 상품 리스트를 전달합니다.
        model.addAttribute("cartItems", cartDetailDtoList);

        // "cart/cartList"라는 이름의 HTML 뷰를 반환합니다.
        // resources/templates/cart/cartList.html 경로의 뷰 파일을 렌더링합니다.
        return "cart/cartList";
    }

    // 장바구니 항목 수량을 업데이트하는 API 엔드포인트입니다.
    @PatchMapping(value = "/cartItem/{cartItemId}") // HTTP PATCH 요청을 "/cartItem/{cartItemId}" 경로로 매핑합니다.
    public @ResponseBody ResponseEntity<?> updateCartItem(
            @PathVariable("cartItemId") Long cartItemId, // URL 경로에서 cartItemId를 변수로 가져옵니다.
            int count, // 요청 본문에서 변경할 수량을 가져옵니다.
            Principal principal // 현재 로그인된 사용자의 정보를 담은 Principal 객체입니다.
    ) {
        // 수량이 0 이하일 경우, 잘못된 요청을 반환합니다.
        if (count <= 0) {
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        }
        // 현재 로그인된 사용자가 해당 장바구니 항목을 수정할 권한이 있는지 확인합니다.
        else if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN); // 권한이 없으면 403 Forbidden 응답을 반환합니다.
        }

        // 수량을 업데이트하는 서비스 메서드를 호출합니다.
        cartService.updateCartItemCount(cartItemId, count);

        // 수량 업데이트가 성공하면 200 OK 응답과 함께 cartItemId를 반환합니다.
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    // 장바구니 항목을 삭제하는 API 엔드포인트입니다.
    @DeleteMapping(value = "/cartItem/{cartItemId}") // HTTP DELETE 요청을 "/cartItem/{cartItemId}" 경로로 매핑합니다.
    public @ResponseBody ResponseEntity<?> deleteCartItem(
            @PathVariable("cartItemId") Long cartItemId, // URL 경로에서 cartItemId를 가져옵니다.
            Principal principal // 현재 로그인된 사용자의 정보를 담은 Principal 객체입니다.
    ) {
        // 현재 로그인된 사용자가 해당 장바구니 항목을 삭제할 권한이 있는지 확인합니다.
        if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            // 권한이 없으면 403 Forbidden 응답을 반환합니다.
            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        // 권한 확인 후 해당 장바구니 항목을 삭제하는 서비스 메서드를 호출합니다.
        cartService.deleteCartItem(cartItemId);

        // 삭제가 성공하면 200 OK 응답과 함께 삭제된 cartItemId를 반환합니다.
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @PostMapping(value = "/cart/orders") // HTTP POST 요청을 "/cart/orders" 경로로 매핑
    public @ResponseBody ResponseEntity<?> orderCartItem(
            @RequestBody CartOrderDto cartOrderDto, // 요청 본문으로부터 CartOrderDto 객체 매핑
            Principal principal // 인증된 사용자의 정보를 담은 Principal 객체
    ) {
        // CartOrderDto 객체에서 주문할 상품 리스트를 가져옴
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        // 주문할 상품 리스트가 비어 있거나 null인 경우 에러 응답 반환
        if (cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
            return new ResponseEntity<String>("주문할 상품을 고르세요", HttpStatus.FORBIDDEN);
        }

        // 각 장바구니 항목에 대해 권한 검증
        for (CartOrderDto cartOrder : cartOrderDtoList) {
            // 현재 로그인한 사용자가 해당 장바구니 항목을 주문할 권한이 있는지 확인
            if (!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())) {
                return new ResponseEntity<String>("주문권한이 없음", HttpStatus.FORBIDDEN);
            }
        }

        // 권한 검증 완료 후, 주문 생성 로직 호출
        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());

        // 주문 ID를 포함한 200 OK 응답 반환
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }


}
