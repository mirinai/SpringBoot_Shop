package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart") // 매핑할 데이터베이스 테이블 명시 (cart)
@Getter @Setter
@ToString
public class Cart {

    @Id // 식별자 필드임을 명시 (카트 고유 번호)
    @Column(name = "cart_id") // 매핑할 데이터베이스 컬럼 명시 (cart_id)
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동으로 값 생성 전략 지정 (GenerationType.AUTO - 데이터베이스 설정에 따라 자동 생성)
    private Long id;

    @OneToOne // 회원과 카트는 일대일 관계임을 명시
    @JoinColumn(name = "member_id") // 외래 키 매핑 - member 테이블의 member_id 컬럼과 연결
    private Member member; // 회원 엔티티 클래스 타입의 필드 (회원과 연관된 카트)
}
