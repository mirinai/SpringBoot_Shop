package com.shop.service;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service // Spring Service 컴포넌트로 등록하여 비즈니스 로직을 처리하는 클래스임을 명시합니다.
@RequiredArgsConstructor // Lombok 어노테이션으로, final 필드에 대한 생성자를 자동 생성합니다.
@Transactional // 메서드나 클래스에 트랜잭션을 적용하여 데이터베이스 작업의 일관성을 보장합니다.
public class CartService {

    private final ItemRepository itemRepository; // 상품 정보를 조회하기 위한 Repository
    private final MemberRepository memberRepository; // 회원 정보를 조회하기 위한 Repository
    private final CartRepository cartRepository; // 장바구니 정보를 관리하기 위한 Repository
    private final CartItemRepository cartItemRepository; // 장바구니 상품 정보를 관리하기 위한 Repository

    // 장바구니에 상품을 추가하는 메서드
    public Long addCart(CartItemDto cartItemDto, String email) {
        // 상품 조회 - 상품 ID를 사용하여 Item 엔티티를 가져옵니다. 없으면 예외 발생
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        // 회원 조회 - 이메일을 사용하여 Member 엔티티를 가져옵니다.
        Member member = memberRepository.findByEmail(email);

        // 장바구니 조회 - 회원 ID를 사용하여 Cart 엔티티를 가져옵니다.
        Cart cart = cartRepository.findByMemberId(member.getId());

        // 장바구니가 없으면 새 장바구니 생성 및 저장
        if (cart == null) {
            cart = Cart.createCart(member); // Cart 엔티티 생성
            cartRepository.save(cart); // 새 장바구니를 DB에 저장
        }

        // 장바구니에 해당 상품이 이미 있는지 확인
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        // 상품이 이미 장바구니에 있으면 수량 추가 후 ID 반환
        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount()); // 수량 추가
            return savedCartItem.getId(); // 기존 장바구니 상품 ID 반환
        }
        // 상품이 장바구니에 없으면 새 CartItem을 생성하여 저장
        else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount()); // 새 CartItem 생성
            cartItemRepository.save(cartItem); // 새로운 CartItem을 DB에 저장
            return cartItem.getId(); // 새로 생성된 CartItem ID 반환
        }
    }

    @Transactional(readOnly = true) // 읽기 전용 트랜잭션 적용: 데이터 조회 시 성능 향상을 위해 읽기 전용 모드로 설정합니다.
    public List<CartDetailDto> getCartList(String email) {
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>(); // 장바구니 정보를 담을 DTO 리스트를 생성합니다.

        Member member = memberRepository.findByEmail(email); // 이메일을 사용하여 회원 정보를 조회합니다.

        Cart cart = cartRepository.findByMemberId(member.getId()); // 회원 ID를 사용하여 해당 회원의 장바구니를 조회합니다.

        if (cart == null) { // 해당 회원의 장바구니가 없을 경우
            return cartDetailDtoList; // 비어 있는 리스트를 반환하여 장바구니가 비어 있음을 알립니다.
        }

        // 장바구니 ID를 사용하여 장바구니 상세 정보를 조회하고 DTO 리스트에 담습니다.
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList; // 장바구니 상세 정보 리스트를 반환합니다.
    }


}
