package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO(Data Transfer Object) 클래스
 * - 상품 정보를 전달하기 위한 객체
 */
@Getter @Setter // Lombok을 사용해 getter와 setter 메서드를 자동 생성
public class MainItemDto {

    /**
     * 상품 ID
     */
    private Long id;

    /**
     * 상품 이름
     */
    private String itemNm;

    /**
     * 상품 상세 설명
     */
    private String itemDetail;

    /**
     * 상품 이미지 URL
     */
    private String imgUrl;

    /**
     * 상품 가격
     */
    private Integer price;

    /**
     * Querydsl에서 사용하는 생성자
     * - @QueryProjection을 사용해 Querydsl 쿼리 결과를 이 DTO로 매핑할 수 있도록 지원
     *
     * @param id 상품 ID
     * @param itemNm 상품 이름
     * @param itemDetail 상품 상세 설명
     * @param imgUrl 상품 이미지 URL
     * @param price 상품 가격
     */
    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price) {
        this.id = id;               // 상품 ID 초기화
        this.itemNm = itemNm;       // 상품 이름 초기화
        this.itemDetail = itemDetail; // 상품 상세 설명 초기화
        this.imgUrl = imgUrl;       // 상품 이미지 URL 초기화
        this.price = price;         // 상품 가격 초기화
    }
}