package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity // JPA의 엔티티(Entity)임을 명시하며, 이 클래스는 데이터베이스 테이블과 매핑됩니다.
@Getter @Setter
public class OrderItem {

    @Id // 이 필드가 테이블의 기본 키(Primary Key)임을 명시합니다.
    @GeneratedValue // 기본 키 값을 자동으로 생성(자동 증가)합니다.
    @Column(name = "order_item_id") // 테이블의 컬럼 이름을 "order_item_id"로 매핑합니다.
    private Long id; // 주문 항목의 고유 식별자(ID)입니다.

    @ManyToOne // OrderItem과 Item 사이의 다대일(N:1) 관계를 설정합니다. 여러 개의 OrderItem이 하나의 Item에 매핑될 수 있습니다.
    @JoinColumn(name = "item_id") // 외래 키(Foreign Key)로 "item_id" 컬럼에 매핑합니다.
    private Item item; // 주문 항목에 연결된 상품(Item) 객체를 나타냅니다.

    @ManyToOne // OrderItem과 Order 사이의 다대일(N:1) 관계를 설정합니다. 여러 개의 OrderItem이 하나의 Order에 매핑될 수 있습니다.
    @JoinColumn(name = "order_id") // 외래 키(Foreign Key)로 "order_id" 컬럼에 매핑합니다.
    // **order_id 외래 키(Foreign Key)**로 매핑합니다.
    // Order 테이블의 기본 키(PK)를 OrderItem 테이블의 order_id 컬럼에 외래 키로 저장합니다.
    private Order order; // 주문 항목이 속한 주문(Order) 객체를 나타냅니다.

    private int orderPrice; // 주문한 상품의 가격을 저장하는 필드입니다. 할인이나 프로모션 적용 후의 최종 가격일 수 있습니다.

    private int count; // 주문한 상품의 수량을 나타내는 필드입니다.

    private LocalDateTime regTime; // 주문 항목이 생성된 날짜 및 시간을 저장하는 필드입니다.

    private LocalDateTime updateTime; // 주문 항목 정보가 마지막으로 수정된 날짜 및 시간을 저장하는 필드입니다.
}
