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
import org.thymeleaf.util.StringUtils;

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


    // 장바구니 항목의 소유자를 검증하는 메서드입니다.
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션으로 설정하여 데이터 변경 없이 조회만 수행합니다.
    public boolean validateCartItem(Long cartItemId, String email) {
        // 이메일을 사용하여 현재 로그인된 회원 정보를 가져옵니다.
        Member member = memberRepository.findByEmail(email);

        // cartItemId를 사용하여 장바구니 항목을 조회합니다. 항목이 없으면 예외를 발생시킵니다.
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        // 장바구니 항목에 연관된 회원 정보를 가져옵니다.
        Member savedMember = cartItem.getCart().getMember();

        // 로그인된 회원과 장바구니 항목 소유자가 일치하지 않으면 false 반환
        if (!StringUtils.equals(member.getEmail(), savedMember.getEmail())) {
            return false; // 소유자가 다르면 false 반환
        }

        return true; // 소유자가 같으면 true 반환
    }

    // 장바구니 항목의 수량을 업데이트하는 메서드입니다.
    public void updateCartItemCount(Long cartItemId, int count) {
        // cartItemId를 사용하여 장바구니 항목을 조회합니다. 항목이 없으면 예외를 발생시킵니다.
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        // 조회된 장바구니 항목의 수량을 새로운 값으로 업데이트합니다.
        cartItem.updateCount(count);
    }

    // 장바구니 항목을 삭제하는 메서드입니다.
    public void deleteCartItem(Long cartItemId) {
        // cartItemId를 사용하여 장바구니 항목을 조회합니다.
        // 장바구니 항목이 존재하지 않을 경우 EntityNotFoundException 예외를 발생시킵니다.
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        // 조회된 장바구니 항목을 데이터베이스에서 삭제합니다.
        cartItemRepository.delete(cartItem); // JPA를 사용하여 해당 CartItem 엔티티를 제거합니다.
    }


}
