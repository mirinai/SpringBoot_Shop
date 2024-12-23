package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

    private String searchDateType;
    /*
     * 검색할 상품 등록일 범위
     * - all: 전체
     * - 1d: 최근 하루
     * - 1w: 최근 일주일
     * - 1m: 최근 한 달
     * - 6m: 최근 6달
     */

    private ItemSellStatus searchSellStatus;
    // 상품 판매 상태 기준 검색

    private String searchBy;
    /*
     * 검색 유형
     * - itemNm: 상품 이름
     * - createdBy: 상품 등록자 아이디
     */

    private String searchQuery = "";
    // 검색어 (itemNm: 상품 이름 / createdBy: 등록자 아이디)
}
