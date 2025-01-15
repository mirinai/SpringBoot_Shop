package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity // JPA 엔티티임을 나타내며, 이 클래스는 데이터베이스 테이블과 매핑됩니다.
@Getter @Setter // Lombok 어노테이션으로 getter, setter 메서드를 자동 생성합니다.
@Table(name = "cart_item") // 데이터베이스 테이블 이름을 "cart_item"으로 지정합니다.
public class CartItem extends BaseEntity { // BaseEntity를 상속받아 생성일, 수정일 정보를 포함합니다.

    @Id // 해당 필드가 Primary Key(기본 키)임을 나타냅니다.
    @GeneratedValue // Primary Key 값을 자동으로 생성하도록 설정합니다.
    @Column(name = "cart_item_id") // 데이터베이스 컬럼 이름을 "cart_item_id"로 매핑합니다.
    private Long id; // 장바구니 항목의 고유 식별자(ID) 필드입니다.

    @ManyToOne(fetch = FetchType.LAZY) // Cart와 다대일(N:1) 관계를 설정합니다. 여러 CartItem이 하나의 Cart에 속할 수 있습니다.
    @JoinColumn(name = "cart_id") // 외래 키를 "cart_id" 컬럼으로 지정하여 Cart와 매핑합니다.
    private Cart cart; // 장바구니(Cart) 엔티티와 연관된 필드입니다.

    @ManyToOne(fetch = FetchType.LAZY) // Item과 다대일(N:1) 관계를 설정합니다. 여러 CartItem이 하나의 Item에 속할 수 있습니다.
    @JoinColumn(name = "item_id") // 외래 키를 "item_id" 컬럼으로 지정하여 Item과 매핑합니다.
    private Item item; // 상품(Item) 엔티티와 연관된 필드로, 장바구니에 담긴 상품을 나타냅니다.

    private int count; // 장바구니에 담긴 해당 상품의 수량을 나타내는 필드입니다.

    // CartItem 객체를 생성하는 정적 팩토리 메서드입니다.
    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem(); // 새로운 CartItem 객체를 생성합니다.
        cartItem.setCart(cart); // 장바구니(Cart) 설정
        cartItem.setItem(item); // 상품(Item) 설정
        cartItem.setCount(count); // 수량 설정
        return cartItem; // 생성된 CartItem 객체 반환
    }

    // 장바구니에 상품 수량을 추가하는 메서드입니다.
    public void addCount(int count) {
        this.count += count; // 기존 수량에 더해 새로운 수량을 추가합니다.
    }

    // 장바구니에 담긴 상품의 수량을 변경하는 메서드입니다.
    public void updateCount(int count) {
        this.count = count; // 해당 장바구니 항목의 수량을 새로운 값으로 설정합니다.
    }

}
