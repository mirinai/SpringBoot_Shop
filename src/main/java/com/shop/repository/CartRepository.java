package com.shop.repository;

import com.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

// CartRepository는 JPA를 사용하여 Cart 엔티티에 대한 데이터베이스 CRUD 작업을 처리하는 인터페이스입니다.
public interface CartRepository extends JpaRepository<Cart, Long> { // JpaRepository를 상속하여 기본적인 CRUD 기능을 제공합니다.

    // 회원 ID를 기반으로 장바구니 정보를 조회하는 메서드입니다.
    // 메서드 이름을 통해 JPA가 자동으로 쿼리를 생성합니다.
    Cart findByMemberId(Long memberId); // 특정 회원의 장바구니(Cart)를 조회합니다.
}
