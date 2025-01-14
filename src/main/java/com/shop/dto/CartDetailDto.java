package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

// 장바구니 상세 정보를 담는 DTO 클래스
@Setter @Getter
public class CartDetailDto {

    private Long cartItemId;  // 장바구니 항목 ID

    private String itemNm;  // 상품명

    private int price;  // 상품 가격

    private int count;  // 상품 수량

    private String imgUrl;  // 상품 이미지 URL

    // 생성자: 장바구니 항목의 필수 정보를 초기화
    private CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl) {
        this.cartItemId = cartItemId;  // 장바구니 항목 ID 설정
        this.itemNm = itemNm;  // 상품명 설정
        this.price = price;  // 상품 가격 설정
        this.count = count;  // 상품 수량 설정
        this.imgUrl = imgUrl;  // 상품 이미지 URL 설정
    }
}
