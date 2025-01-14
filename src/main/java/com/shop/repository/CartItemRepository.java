package com.shop.repository;

import com.shop.dto.CartDetailDto;
import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// 장바구니 항목을 관리하는 Repository 인터페이스
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // cartId와 itemId를 기준으로 장바구니 항목을 조회하는 메서드
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    /**
     * 장바구니에 있는 항목들의 상세 정보를 가져오는 JPQL 쿼리
     * - CartDetailDto 생성자를 사용하여 필요한 필드를 조회 및 매핑
     * - CartItem(ci)와 ItemImg(im)를 조인하여 대표 이미지 URL을 가져옴
     * - 조건:
     *   1. 장바구니 ID가 :cartId인 항목
     *   2. 해당 상품의 대표 이미지 (repImgYn = 'Y')만 가져옴
     * - 등록 시간(regTime)을 기준으로 내림차순 정렬
     */
    @Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +  // CartItem과 ItemImg 엔티티를 사용
            "join ci.item i " +  // CartItem과 Item을 조인
            "where ci.cart.id = :cartId " +  // 장바구니 ID가 일치하는 조건
            "and im.item.id = ci.item.id " +  // ItemImg의 item.id가 CartItem의 item.id와 일치
            "and im.repImgYn = 'Y' " +  // 대표 이미지 여부 확인 (Y)
            "order by ci.regTime desc")  // 등록 시간 기준 내림차순 정렬
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
