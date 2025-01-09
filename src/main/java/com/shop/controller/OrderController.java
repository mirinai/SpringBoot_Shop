package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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

    // 주문 내역 페이지 조회 메서드
    @GetMapping(value = {"/orders", "/orders/{page}"})
// "/orders" 또는 "/orders/{page}" 경로로 GET 요청을 처리
    public String orderHist(@PathVariable(value = "page", required = false) Integer page, // URL 경로에서 페이지 번호를 가져옴 (없을 경우 null)
                            Principal principal, // 현재 인증된 사용자의 정보를 가져옴
                            Model model) { // 뷰에 데이터를 전달하기 위한 객체

        // 페이지 번호가 null이면 0페이지를 기본값으로 설정, 한 페이지에 4개의 데이터 표시
        Pageable pageable = PageRequest.of(page != null ? page : 0, 4);

        // 로그인한 사용자의 이메일을 기반으로 주문 내역을 페이징 처리하여 가져옴
        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        // 뷰에 전달할 데이터 설정
        model.addAttribute("orders", orderHistDtoList); // 주문 내역 리스트
        model.addAttribute("page", pageable.getPageNumber()); // 현재 페이지 번호
        model.addAttribute("maxPage", 5); // 페이지 네비게이션에서 최대 표시할 페이지 수

        // "order/orderHist.html" 템플릿 파일을 반환하여 주문 내역 페이지 렌더링
        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
// HTTP POST 요청을 "/order/{orderId}/cancel" 경로로 매핑합니다.
// 주문 취소 요청을 처리하는 메서드입니다.
    public @ResponseBody ResponseEntity cancelOrder
            (@PathVariable("orderId") Long orderId, Principal principal) {
        // 📝 [메서드 설명]
        // - 특정 주문을 취소하는 메서드입니다.
        // - 주문 ID를 URL 경로에서 가져오고, 현재 로그인한 사용자의 이메일을 Principal 객체에서 가져옵니다.
        // - 권한 검사를 통해 해당 사용자가 주문을 취소할 권한이 있는지 확인한 후 주문을 취소합니다.
        // - JSON 응답을 반환하기 위해 @ResponseBody를 사용합니다.

        if (!orderService.validateOrder(orderId, principal.getName())) {
            // 현재 로그인한 사용자가 해당 주문을 취소할 권한이 있는지 확인
            // 권한이 없는 경우 HTTP 상태 코드 403(FORBIDDEN)과 메시지를 반환
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        // 주문 취소 권한이 있는 경우 주문 취소 서비스 메서드 호출
        orderService.cancelOrder(orderId);

        // 취소된 주문 ID와 HTTP 상태 코드 200(OK)을 응답으로 반환
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}
