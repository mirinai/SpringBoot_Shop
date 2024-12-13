package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity // JPA의 엔티티(Entity)임을 명시하는 어노테이션으로, 이 클래스는 데이터베이스 테이블과 매핑됩니다.
@Getter @Setter // 롬복(Lombok) 어노테이션으로, getter와 setter 메서드를 자동 생성합니다.
@Table(name = "cart_item") // 테이블과 매핑할 이름을 "cart_item"으로 지정합니다.
public class CartItem {

    @Id // 이 필드가 테이블의 기본 키(Primary Key)임을 명시합니다.
    @GeneratedValue // 기본 키 값을 자동으로 생성(자동 증가)합니다.
    @Column(name = "cart_item_id") // 테이블의 컬럼 이름을 "cart_item_id"로 매핑합니다.
    private Long id; // 장바구니 아이템의 고유 식별자(ID)입니다.

    @ManyToOne(fetch = FetchType.LAZY) // Cart와의 다대일(N:1) 관계를 설정합니다. 여러 CartItem이 하나의 Cart에 매핑될 수 있습니다.
    @JoinColumn(name = "cart_id") // 외래 키(Foreign Key)로 "cart_id" 컬럼에 매핑합니다.
    private Cart cart; // 장바구니(Cart)와의 연관 관계를 나타내는 필드입니다.

    @ManyToOne(fetch = FetchType.LAZY) // Item과의 다대일(N:1) 관계를 설정합니다. 여러 CartItem이 하나의 Item에 매핑될 수 있습니다.
    @JoinColumn(name = "item_id") // 외래 키(Foreign Key)로 "item_id" 컬럼에 매핑합니다.
    private Item item; // 장바구니 항목에 연결된 상품(Item)입니다. 하나의 Item에 여러 CartItem이 연결될 수 있습니다.

    private int count; // 장바구니에 담긴 상품의 수량을 나타내는 필드입니다.

}
