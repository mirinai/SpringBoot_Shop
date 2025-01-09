package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity // JPA의 엔티티(Entity)임을 명시하며, 이 클래스는 데이터베이스 테이블과 매핑됩니다.
@Getter @Setter
public class OrderItem extends BaseEntity{

    @Id // 이 필드가 테이블의 기본 키(Primary Key)임을 명시합니다.
    @GeneratedValue // 기본 키 값을 자동으로 생성(자동 증가)합니다.
    @Column(name = "order_item_id") // 테이블의 컬럼 이름을 "order_item_id"로 매핑합니다.
    private Long id; // 주문 항목의 고유 식별자(ID)입니다.

    @ManyToOne(fetch = FetchType.LAZY)
    // OrderItem과 Item 사이의 다대일(N:1) 관계를 설정합니다. 여러 개의 OrderItem이 하나의 Item에 매핑될 수 있습니다.
    // **fetch = FetchType.LAZY**: 지연 로딩 전략을 적용하여,
    // OrderItem을 조회할 때 Item은 로드되지 않고, **필요할 때만 조회**됩니다.
    // 실제로 **item.getXXX() 메서드를 호출할 때 프록시가 초기화**되어 **데이터베이스에서 조회**가 일어납니다.
    @JoinColumn(name = "item_id") // 외래 키(Foreign Key)로 "item_id" 컬럼에 매핑합니다.
    private Item item; // 주문 항목에 연결된 상품(Item) 객체를 나타냅니다.

    @ManyToOne(fetch = FetchType.LAZY)
    // OrderItem과 Order 사이의 다대일(N:1) 관계를 설정합니다. 여러 개의 OrderItem이 하나의 Order에 매핑될 수 있습니다.
    // **fetch = FetchType.LAZY**: 지연 로딩 전략을 적용하여,
    // OrderItem을 조회할 때 Order는 로드되지 않고, **필요할 때만 조회**됩니다.
    // **order.getXXX() 메서드를 호출할 때 프록시가 초기화**되어 **데이터베이스에서 조회**가 일어납니다.
    @JoinColumn(name = "order_id") // 외래 키(Foreign Key)로 "order_id" 컬럼에 매핑합니다.
    // Order 테이블의 기본 키(PK)를 OrderItem 테이블의 order_id 컬럼에 외래 키로 저장합니다.
    private Order order; // 주문 항목이 속한 주문(Order) 객체를 나타냅니다.

    private int orderPrice; // 주문한 상품의 가격을 저장하는 필드입니다. 할인이나 프로모션 적용 후의 최종 가격일 수 있습니다.

    private int count; // 주문한 상품의 수량을 나타내는 필드입니다.

//    private LocalDateTime regTime; // 주문 항목이 생성된 날짜 및 시간을 저장하는 필드입니다.
//
//    private LocalDateTime updateTime; // 주문 항목 정보가 마지막으로 수정된 날짜 및 시간을 저장하는 필드입니다.

    public static OrderItem createOrderItem(Item item, int count){
        // OrderItem 객체를 생성하는 정적 팩토리 메서드
        OrderItem orderItem = new OrderItem(); // OrderItem 인스턴스 생성
        orderItem.setItem(item); // 생성된 OrderItem에 구매할 Item 객체 설정
        orderItem.setCount(count); // 구매 수량 설정
        orderItem.setOrderPrice(item.getPrice()); // 아이템의 가격 설정

        item.removeStock(count); // 구매한 수량만큼 재고 차감

        return orderItem; // 완성된 OrderItem 객체 반환
    }

    public int getTotalPrice(){
        // 총 가격 계산 메서드: 주문 가격(orderPrice)과 수량(count)을 곱함
        return orderPrice*count;
    }

    // 주문 취소하면 주문 수량만큼 상품의 재고를 더해줌
    public void cancel(){
        this.getItem().addStock(count);
    }
}
