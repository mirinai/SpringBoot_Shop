package com.shop.dto;

import com.shop.constant.OrderStatus;
import com.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {

    // 생성자: Order 객체를 받아서 OrderHistDto의 필드를 초기화합니다.
    public OrderHistDto(Order order) {
        this.orderId = order.getId(); // 주문의 고유 식별자 설정
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        // 주문 날짜를 "yyyy-MM-dd HH:mm" 형식으로 포맷하여 설정 (예: "2025-01-01 14:30")
        this.orderStatus = order.getOrderStatus(); // 주문 상태 설정 (ENUM 값: ORDER, CANCEL 등)
    }

    private Long orderId; // 주문 ID (고유 식별자)

    private String orderDate; // 주문 날짜 및 시간 (포맷된 문자열 값)

    private OrderStatus orderStatus; // 주문 상태 (ENUM 타입: 주문 상태를 나타냄)

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();
    // 주문 상품 정보를 담을 DTO 리스트. 하나의 주문에 여러 상품이 포함될 수 있음.

    // 주문 상품을 DTO 리스트에 추가하는 메서드
    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto); // 주문 상품 정보를 리스트에 추가
    }
}


