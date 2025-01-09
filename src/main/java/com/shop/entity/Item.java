package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 📘 **상품 엔티티 클래스 (Item)**
 *
 * 🛠️ **주요 역할**
 * - **상품 정보 관리**: 상품의 이름, 가격, 재고, 판매 상태, 상세 설명을 포함
 * - **BaseEntity**를 상속받아 등록 시간, 수정 시간을 자동으로 관리
 */
@Entity // 이 클래스가 JPA 엔티티임을 나타냄
@Table(name = "item") // 데이터베이스 테이블 이름을 지정
@Getter // Lombok 어노테이션: getter 메서드 자동 생성
@Setter // Lombok 어노테이션: setter 메서드 자동 생성
@ToString // Lombok 어노테이션: toString 메서드 자동 생성
public class Item extends BaseEntity{

    @Id // 엔티티의 기본 키를 나타냄
    @Column(name = "item_id") // 데이터베이스 테이블의 컬럼 이름 지정
    @GeneratedValue(strategy = GenerationType.AUTO) // 기본 키 값을 자동으로 생성하는 방식 지정
    private Long id; // 상품 코드 (기본 키)

    @Column(nullable = false, length = 50) // 필수 입력 필드로 최대 길이를 50자로 제한
    private String itemNm; // 상품 이름

    @Column(name = "price", nullable = false) // 필수 입력 필드로 지정
    private int price; // 상품 가격

    @Column(nullable = false) // 필수 입력 필드로 지정
    private int stockNumber; // 상품 재고 수량

    @Lob // 큰 데이터(텍스트) 타입을 저장하기 위한 설정
    @Column(nullable = false) // 필수 입력 필드로 지정
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING) // 열거형(Enum)을 문자열로 저장
    private ItemSellStatus itemSellStatus; // 상품 판매 상태 (SELL, SOLD_OUT)

//    private LocalDateTime regTime; // 등록 시간
//
//    private LocalDateTime updateTime; // 수정 시간


    public void updateItem(ItemFormDto itemFormDto) {
        // 📝 [메서드 설명]
        // - 상품 정보를 업데이트하는 메서드
        // - ItemFormDto 객체의 정보를 받아서 해당 Item 엔티티의 필드를 변경

        // 🛠️ [업데이트 내용]
        // - 상품 이름, 가격, 재고 수량, 상세 설명, 판매 상태를 변경
        this.itemNm = itemFormDto.getItemNm(); // 상품 이름 업데이트
        this.price = itemFormDto.getPrice(); // 상품 가격 업데이트
        this.stockNumber = itemFormDto.getStockNumber(); // 재고 수량 업데이트
        this.itemDetail = itemFormDto.getItemDetail(); // 상품 상세 설명 업데이트
        this.itemSellStatus = itemFormDto.getItemSellStatus(); // 상품 판매 상태 업데이트 (판매 중/품절)
    }

    public void removeStock(int stockNumber) {
        // 📝 [메서드 설명]
        // - 상품 재고를 감소시키는 메서드
        // - 요청된 수량만큼 재고를 줄이고, 남은 재고가 0보다 작아질 경우 예외를 발생시킴

        int restStock = this.stockNumber - stockNumber; // 남은 재고 계산

        // ❗ [예외 처리]
        // - 재고 부족 시 OutOfStockException 발생
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 없습니다. (현재 재고 수량: " + this.stockNumber + ")");
        }

        this.stockNumber = restStock; // 재고 수량 업데이트
    }

    public void addStock(int stockNumber) {
        // 📝 [메서드 설명]
        // - 상품 재고를 증가시키는 메서드
        // - 주문 취소 시 해당 상품의 재고를 복구하는 용도로 사용됨

        this.stockNumber += stockNumber; // 재고 수량을 요청된 수량만큼 증가
    }

}

