package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartOrderDto {

    // 장바구니 아이템의 ID를 저장하는 필드
    private Long cartItemId;

    // 여러 장바구니 아이템의 주문 정보를 저장하는 리스트 필드
    private List<CartOrderDto> cartOrderDtoList;
}
