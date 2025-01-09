package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    // 생성자: OrderItem 객체와 이미지 URL을 받아서 OrderItemDto의 필드를 초기화합니다.
    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        // 상품 이름을 OrderItem의 Item 객체에서 가져옴
        this.itemNm = orderItem.getItem().getItemNm();
        // 주문 수량 설정
        this.count = orderItem.getCount();
        // 주문 가격 설정
        this.orderPrice = orderItem.getOrderPrice();
        // 상품 이미지 URL 설정
        this.imgUrl = imgUrl;
    }

    // 상품 이름
    private String itemNm;

    // 주문 수량
    private int count;

    // 주문 가격
    private int orderPrice;

    // 상품 이미지 경로
    private String imgUrl;

}
