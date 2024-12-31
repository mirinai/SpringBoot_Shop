package com.shop.entity;

import com.shop.constant.OrderStatus; // 주문 상태에 대한 enum 클래스를 가져옵니다.
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // JPA의 엔티티(Entity)임을 명시하며, 이 클래스는 데이터베이스 테이블과 매핑됩니다.
@Table(name = "orders") // 테이블과 매핑할 이름을 "orders"로 지정합니다.
@Getter @Setter // 롬복(Lombok) 어노테이션으로, getter와 setter 메서드를 자동 생성합니다.
public class Order extends BaseEntity{

    @Id // 이 필드가 테이블의 기본 키(Primary Key)임을 명시합니다.
    @GeneratedValue // 기본 키 값을 자동으로 생성(자동 증가)합니다.
    @Column(name = "order_id") // 테이블의 컬럼 이름을 "order_id"로 매핑합니다.
    private Long id; // 주문의 고유 식별자(ID)입니다.

    @ManyToOne(fetch = FetchType.LAZY) // Order와 Member 사이의 다대일(N:1) 관계를 설정합니다. 여러 주문이 하나의 회원에 매핑될 수 있습니다.
    @JoinColumn(name = "member_id") // 외래 키(Foreign Key)로 "member_id" 컬럼에 매핑합니다.
    private Member member; // 주문과 연결된 회원(Member) 객체를 나타냅니다.

    private LocalDateTime orderDate; // 주문 날짜 및 시간을 나타내는 필드입니다.

    @Enumerated(EnumType.STRING) // Enum을 데이터베이스에 **문자열(String)**로 저장합니다.
    private OrderStatus orderStatus; // 주문 상태를 나타내는 열거형 필드로, 예를 들어 "ORDER", "CANCEL"과 같은 상태 값이 들어갑니다.

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    // Order와 OrderItem 간의 **1:N(일대다) 관계**를 설정합니다.
    // OrderItem의 `order` 필드에 의해 매핑되며, **Order는 연관 관계의 주인이 아닙니다.**
    // **cascade = CascadeType.ALL** : Order(부모 엔티티)의 상태 변화(PERSIST, MERGE, REMOVE 등)가
    // **연관된 OrderItem(자식 엔티티)에게 모두 전이**됩니다.
    // **orphanRemoval = true** : Order의 orderItems 컬렉션에서 OrderItem을 제거하면
    // **고아 객체가 되어 자동으로 DELETE 쿼리가 실행**됩니다.
    private List<OrderItem> orderItems = new ArrayList<>();
    // **하나의 주문(Order)에는 여러 개의 주문 항목(OrderItem)이 포함**될 수 있습니다.


//    private LocalDateTime regTime; // 주문이 처음 생성된 날짜 및 시간을 저장하는 필드입니다.
//
//    private LocalDateTime updateTime; // 주문 정보가 마지막으로 수정된 날짜 및 시간을 저장하는 필드입니다.


    // 주문 항목(OrderItem)을 주문(Order)에 추가하는 메서드
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem); // 주문 항목 리스트에 새로운 주문 항목 추가
        orderItem.setOrder(this); // 해당 주문 항목에 현재 주문(Order) 객체를 설정
    }

    // 주문(Order)을 생성하는 정적 팩토리 메서드
    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order(); // 새로운 주문 객체 생성

        order.setMember(member); // 주문한 회원(Member) 객체 설정

        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem); // 전달받은 주문 항목 리스트를 주문 객체에 추가
        }

        order.setOrderStatus(OrderStatus.ORDER); // 주문 상태를 'ORDER'로 설정
        order.setOrderDate(LocalDateTime.now()); // 주문 날짜를 현재 시간으로 설정

        return order; // 생성된 주문 객체 반환
    }

    // 총 주문 금액을 계산하는 메서드
    public int getTotalPrice() {
        int totalPrice = 0; // 총 가격 초기화
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice(); // 각 주문 항목의 총 가격을 더함
        }
        return totalPrice; // 계산된 총 가격 반환
    }

}
