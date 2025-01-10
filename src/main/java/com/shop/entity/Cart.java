package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // JPA에서 엔티티 클래스임을 명시 (DB 테이블과 매핑됨)
@Table(name = "cart") // 매핑할 데이터베이스 테이블 명시 (cart)
@Getter @Setter
@ToString
public class Cart extends BaseEntity { // BaseEntity를 상속받아 생성일, 수정일 필드를 함께 사용

    @Id // 이 필드가 식별자(Primary Key) 필드임을 명시
    @Column(name = "cart_id") // 매핑할 데이터베이스 컬럼 명시 (cart_id)
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동으로 Primary Key 값 생성 전략 지정 (AUTO: 데이터베이스에 따라 자동 생성)
    private Long id; // 카트 고유 번호 (Primary Key)

    @OneToOne(fetch = FetchType.LAZY) // 회원과 카트는 1:1 관계임을 명시, LAZY 로딩 설정 (필요할 때 연관 엔티티 로드)
    @JoinColumn(name = "member_id") // 외래 키 매핑 (member 테이블의 member_id 컬럼과 연결)
    private Member member; // 연관된 회원 엔티티 필드 (회원과 연결된 카트 정보)

    // 정적 메서드 - 회원과 연관된 새로운 Cart 객체를 생성하는 메서드
    public static Cart createCart(Member member) {
        Cart cart = new Cart(); // Cart 객체 생성
        cart.setMember(member); // 연관된 회원 설정
        return cart; // 생성된 Cart 객체 반환
    }
}
