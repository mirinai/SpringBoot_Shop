package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 사용자 정의 리포지토리 인터페이스.
 * Querydsl을 활용하여 관리자용 상품 페이지를 동적으로 조회하는 메서드를 정의합니다.
 */
public interface ItemRepositoryCustom {
    /**
     * 관리자 화면에서 상품 관리 페이지를 조회하기 위한 메서드.
     *
     * @param itemSearchDto 상품 검색 조건을 담고 있는 DTO
     * @param pageable 페이지 정보 (페이지 번호, 사이즈 등)
     * @return 검색 조건에 맞는 상품 데이터와 페이지 정보를 담은 Page 객체
     */
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}

