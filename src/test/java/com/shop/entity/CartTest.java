package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 부트 테스트 환경을 설정하는 어노테이션입니다.
@Transactional // 테스트가 끝난 후 데이터베이스 변경 사항을 롤백합니다.
@TestPropertySource(locations = "classpath:application-test.properties") // 테스트 환경을 위한 프로퍼티 파일을 설정합니다.
class CartTest {

    @Autowired
    CartRepository cartRepository; // CartRepository를 의존성 주입합니다.

    @Autowired
    MemberRepository memberRepository; // MemberRepository를 의존성 주입합니다.

    @Autowired
    PasswordEncoder passwordEncoder; // 비밀번호를 암호화하는 PasswordEncoder를 의존성 주입합니다.

    @PersistenceContext
    EntityManager entityManager; // 엔티티 매니저를 주입하여 데이터베이스와 상호작용할 수 있습니다.

    /**
     * 새로운 회원(Member) 엔티티를 생성하는 메서드입니다.
     * @return 생성된 Member 엔티티
     */
    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto(); // 회원 정보 DTO 객체를 생성합니다.

        memberFormDto.setEmail("test@email.com"); // 이메일 설정
        memberFormDto.setName("라라 크로프트"); // 이름 설정
        memberFormDto.setAddress("잉글랜드"); // 주소 설정
        memberFormDto.setPassword("1234"); // 비밀번호 설정

        // Member 엔티티를 생성하여 반환합니다. 비밀번호는 PasswordEncoder를 통해 암호화합니다.
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    /**
     * 장바구니(Cart)와 회원(Member) 간의 매핑을 테스트하는 메서드입니다.
     */
    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트") // 테스트의 의미를 설명하는 어노테이션입니다.
    public void findCartAndMemberTest(){

        // 새로운 회원 엔티티를 생성하고 데이터베이스에 저장합니다.
        Member member = createMember(); // 회원 엔티티 생성

        memberRepository.save(member); // 생성한 회원 엔티티를 저장합니다.

        // 새로운 장바구니 엔티티를 생성하고 회원과 매핑합니다.
        Cart cart = new Cart(); // 장바구니 엔티티 생성

        cart.setMember(member); // 장바구니에 회원을 매핑합니다.

        cartRepository.save(cart); // 장바구니 엔티티를 저장합니다.

        // 영속성 컨텍스트의 변경 사항을 데이터베이스에 반영하고, 영속성 컨텍스트를 초기화합니다.
        entityManager.flush(); // 변경 사항을 데이터베이스에 강제로 반영합니다.
        entityManager.clear(); // 영속성 컨텍스트를 초기화하여 엔티티 캐시를 비웁니다.

        // 저장된 장바구니 엔티티를 데이터베이스에서 다시 조회합니다.
        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new); // 장바구니 엔티티가 없으면 예외를 던집니다.

        // 저장된 장바구니 엔티티에 매핑된 회원의 ID와 원래 회원의 ID가 일치하는지 확인합니다.
        assertEquals(savedCart.getMember().getId(), member.getId()); // 매핑이 올바른지 검증합니다.
    }
}
