package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * ItemRepository를 테스트하기 위한 테스트 클래스
 */
@SpringBootTest // Spring Boot 애플리케이션 컨텍스트를 로드하여 통합 테스트를 수행
@TestPropertySource(locations = "classpath:application-test.properties") // 테스트 시 사용할 프로퍼티 파일 지정
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository; // 테스트할 ItemRepository 주입
    
    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){

        // 새로운 Item 객체 생성
        Item item = new Item();

        // Item 객체에 값 설정
        item.setItemNm("테스트 상품"); // 상품 이름 설정
        item.setPrice(10000); // 가격 설정
        item.setItemDetail("테스트 상품 상세 설명"); // 상세 설명 설정
        item.setItemSellStatus(ItemSellStatus.SELL); // 판매 상태 설정 (SELL)
        item.setStockNumber(100); // 재고 수량 설정
        item.setRegTime(LocalDateTime.now()); // 등록 시간 설정
        item.setUpdateTime(LocalDateTime.now()); // 수정 시간 설정

        // Item 객체를 데이터베이스에 저장하고 저장된 객체 반환
        Item savedItem = itemRepository.save(item);

        // 저장된 Item 객체 출력 (디버깅 용도)
        System.out.println(savedItem.toString());
    }
    /**
     * 테스트용 Item 목록을 생성하여 데이터베이스에 저장하는 메서드
     */
    public void createItemList(){
        for(int i = 1; i <= 10; i++){ // 1부터 10까지 반복
            Item item = new Item(); // 새로운 Item 객체 생성
            item.setItemNm("테스트 상품" + i); // 상품 이름 설정 (예: 테스트 상품1, 테스트 상품2, ...)
            item.setPrice(10000 + i); // 가격 설정 (예: 10001, 10002, ...)
            item.setItemDetail("테스트 상품 상세 설명" + i); // 상세 설명 설정 (예: 테스트 상품 상세 설명1, ...)
            item.setItemSellStatus(ItemSellStatus.SELL); // 판매 상태 설정 (SELL)
            item.setStockNumber(100); // 재고 수량 설정
            item.setRegTime(LocalDateTime.now()); // 등록 시간 설정
            item.setUpdateTime(LocalDateTime.now()); // 수정 시간 설정
            itemRepository.save(item); // Item 객체를 데이터베이스에 저장
        }
    }
    /**
     * 상품 이름으로 Item 리스트를 조회하는 테스트
     */
    @Test
    @DisplayName("상품이름 조회 리스트")
    public void findByItemNmTest(){

        this.createItemList(); // 테스트용 데이터 생성

        // "테스트 상품1"이라는 이름을 가진 Item 리스트 조회
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");

        // 조회된 Item 리스트 출력
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }
    /**
     * 상품명 또는 상품 상세 설명으로 Item 리스트를 조회하는 테스트
     */
    @Test
    @DisplayName("상품명, 상품 상세 설명 or 테스트")
    public void findByItemNmOrItemDetailTest(){
        this.createItemList();  // 테스트용 데이터 생성

        // "테스트 상품1" 또는 "테스트 상품 상세 설명5"인 Item 리스트 조회
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");

        // 조회된 Item 리스트 출력
        for(Item item : itemList){
            System.out.println("-------------------------------------");
            System.out.println(item.toString());
            System.out.println("-------------------------------------");
        }

        // 조회된 아이템 수 출력 (디버깅 용도)
        System.out.println("조회된 아이템 수: " + itemList.size());

        // 추가 검증 (Assertions) - 권장 사항
        assertFalse(itemList.isEmpty(), "아이템 리스트가 비어있습니다."); // 아이템 리스트가 비어있지 않은지 확인

        for(Item item : itemList){
            // 각 아이템의 이름이 "테스트 상품1" 이거나 상세 설명이 "테스트 상품 상세 설명5"인지 확인
            assertTrue(item.getItemNm().equals("테스트 상품1") || item.getItemDetail().equals("테스트 상품 상세 설명5"),
                    "아이템명이 '테스트 상품1' 또는 상세 설명이 '테스트 상품 상세 설명5'가 아닙니다.");
        }
    }
    /**
     * 가격이 특정 값보다 작은 Item 리스트를 조회하는 테스트
     */
    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest(){

        this.createItemList(); // 테스트용 데이터 생성

        // 가격이 10005보다 작은 Item 리스트 조회
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);

        // 조회된 Item 리스트 출력
        for(Item item : itemList){
            System.out.println("-------------------------------------");
            System.out.println(item.toString());
            System.out.println("-------------------------------------");
        }
        // 조회된 아이템 수 출력 (디버깅 용도)
        System.out.println("조회된 아이템 수: " + itemList.size());
    }
    /**
     * 가격이 특정 값보다 작은 Item 리스트를 가격 내림차순으로 조회하는 테스트
     */
    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc(){
        this.createItemList(); // 테스트용 데이터 생성

        // 가격이 10005보다 작은 Item 리스트를 가격 내림차순으로 조회
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);

        // 조회된 Item 리스트 출력
        for(Item item : itemList){
            System.out.println("-------------------------------------");
            System.out.println(item.toString());
            System.out.println("-------------------------------------");
        }

        // 조회된 아이템 수 출력 (디버깅 용도)
        System.out.println("조회된 아이템 수: " + itemList.size());
    }

    @Test
    @DisplayName("@쿼리를 쓴 상품 조회 테스트")
    // JUnit 테스트 메서드를 정의합니다. "@쿼리"를 사용하여 상품을 조회하는 테스트임을 표시합니다.
    public void findByItemDetailTest(){
        // 테스트용 데이터를 생성합니다. 데이터베이스에 테스트 데이터를 삽입하는 메서드를 호출합니다.
        this.createItemList();
        // "테스트 상품 상세 설명"이라는 문자열을 포함한 itemDetail을 가지는 Item 목록을 조회합니다.
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");

        // 조회된 Item 객체들을 순회하면서 각 객체의 정보를 출력합니다.
//        for(Item item : itemList){
//            System.out.println(item.toString());
//        }
        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            if(i>0) {
                System.out.println("----------------------------------");
            }
            System.out.println(item.toString());
            if(i == itemList.size()-1){
                System.out.println("===================================");
            }
        }
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 리스트")
    public void findByDetailByNative(){

        StringBuilder output = new StringBuilder();

        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for(int i = 0;i<itemList.size();i++){
            Item item = itemList.get(i);

            if(i>0){
                output.append("-----------------------------------------------\n");
            }

            output.append(item.toString()).append("\n");

            if(i == itemList.size()-1){
                output.append("=================================================");
            }
        } //for

        System.out.println(output.toString());
    }
}