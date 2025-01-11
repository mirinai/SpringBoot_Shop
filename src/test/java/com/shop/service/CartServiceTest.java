package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.CartItemDto;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 부트 테스트 환경을 설정합니다.
@Transactional // 테스트 메서드가 끝난 후 데이터베이스 변경 사항을 롤백합니다.
@TestPropertySource(locations = "classpath:application-test.properties") // 테스트 설정 파일을 사용합니다.
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository; // 상품 저장소 주입

    @Autowired
    MemberRepository memberRepository; // 회원 저장소 주입

    @Autowired
    CartService cartService; // 장바구니 서비스 주입

    @Autowired
    CartItemRepository cartItemRepository; // 장바구니 항목 저장소 주입

    /**
     * 📘 테스트용 상품 생성 및 저장 메서드
     * 테스트에 사용할 상품 객체를 생성하고 저장합니다.
     *
     * @return 저장된 상품 객체
     */
    public Item savaItem() {
        Item item = new Item(); // 상품 엔티티 생성

        item.setItemNm("테스트 상품"); // 상품명 설정
        item.setPrice(10000); // 상품 가격 설정
        item.setItemDetail("테스트 상품 상세 설명"); // 상품 상세 설명 설정
        item.setItemSellStatus(ItemSellStatus.SELL); // 상품 판매 상태 설정 (판매 중)
        item.setStockNumber(100); // 재고 수량 설정

        return itemRepository.save(item); // 상품을 저장하고 반환
    }

    /**
     * 📘 테스트용 회원 생성 및 저장 메서드
     * 테스트에 사용할 회원 객체를 생성하고 저장합니다.
     *
     * @return 저장된 회원 객체
     */
    public Member saveMember() {
        Member member = new Member(); // 회원 엔티티 생성

        member.setEmail("test@test.com"); // 이메일 설정 (테스트용 고유 값)

        return memberRepository.save(member); // 회원을 저장하고 반환
    }

    /**
     * 📘 장바구니 담기 테스트 메서드
     * 상품을 장바구니에 추가하는 기능을 테스트합니다.
     */
    @Test
    @DisplayName("장바구니 담기 테스트") // 테스트 메서드 설명 표시
    public void addCart() {
        Item item = savaItem(); // 테스트용 상품 생성 및 저장
        Member member = saveMember(); // 테스트용 회원 생성 및 저장

        CartItemDto cartItemDto = new CartItemDto(); // 장바구니 항목 DTO 생성
        cartItemDto.setCount(5); // 장바구니에 담을 상품 수량 설정
        cartItemDto.setItemId(item.getId()); // 상품 ID 설정

        // 장바구니에 상품을 추가하고 해당 장바구니 항목 ID를 반환받음
        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());

        // 반환된 장바구니 항목 ID로 CartItem을 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        // 테스트 검증: 장바구니에 담긴 상품 ID와 수량이 일치하는지 확인
        assertEquals(item.getId(), cartItem.getItem().getId()); // 상품 ID 비교
        assertEquals(cartItemDto.getCount(), cartItem.getCount()); // 수량 비교
    }
}
