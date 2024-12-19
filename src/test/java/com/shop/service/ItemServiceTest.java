package com.shop.service;

import com.shop.constant.ItemSellStatus; // **상품 판매 상태 상수** (SELL, SOLD_OUT 등) import
import com.shop.dto.ItemFormDto; // **상품 등록 폼의 DTO** import
import com.shop.entity.Item; // **상품 엔티티** import
import com.shop.entity.ItemImg; // **상품 이미지 엔티티** import
import com.shop.repository.ItemImgRepository; // **상품 이미지 리포지토리** import
import com.shop.repository.ItemRepository; // **상품 리포지토리** import
import jakarta.persistence.EntityNotFoundException; // **엔티티 조회 실패 예외** import
import org.junit.jupiter.api.DisplayName; // **테스트의 이름을 설정**하는 어노테이션 import
import org.junit.jupiter.api.Test; // **JUnit 테스트 메서드** 어노테이션 import
import org.springframework.beans.factory.annotation.Autowired; // **스프링 빈 자동 주입** 어노테이션 import
import org.springframework.boot.test.context.SpringBootTest; // **Spring Boot 테스트 실행**을 위한 어노테이션 import
import org.springframework.mock.web.MockMultipartFile; // **가짜 MultipartFile**을 생성하기 위한 클래스 import
import org.springframework.security.test.context.support.WithMockUser; // **가짜 사용자 인증**을 위한 어노테이션 import
import org.springframework.test.context.TestPropertySource; // **테스트 환경의 프로퍼티 파일 지정** 어노테이션 import
import org.springframework.transaction.annotation.Transactional; // **트랜잭션 처리** 어노테이션 import
import org.springframework.web.multipart.MultipartFile; // **파일 업로드**를 위해 사용하는 객체

import java.util.ArrayList; // **ArrayList 컬렉션** import
import java.util.List; // **List 인터페이스** import

import static org.junit.jupiter.api.Assertions.*; // **JUnit의 assertion 메서드** import

/**
 * 📘 **ItemServiceTest 클래스**
 *
 * 이 클래스는 **ItemService의 동작을 검증하는 단위 테스트 클래스**입니다.
 *
 * 🛠️ **주요 역할**
 * - 상품 등록 테스트
 * - 상품 이미지 등록 테스트
 */
@SpringBootTest // **Spring Boot 애플리케이션 컨텍스트를 로드**하여 테스트를 실행
@Transactional // **테스트 실행 후 데이터베이스의 변경 사항을 롤백**합니다.
@TestPropertySource(locations = "classpath:application-test.properties") // **테스트 환경의 프로퍼티 파일 지정**
class ItemServiceTest {

    /**
     * 📘 **서비스 및 리포지토리 주입**
     */
    @Autowired
    ItemService itemService; // **ItemService 주입**

    @Autowired
    ItemRepository itemRepository; // **ItemRepository 주입**

    @Autowired
    ItemImgRepository itemImgRepository; // **ItemImgRepository 주입**

    /**
     * 📘 **테스트용 MultipartFile 생성 메서드**
     *
     * @return **MockMultipartFile의 리스트** (가짜 파일 리스트)
     *
     * 🛠️ **주요 동작**
     * - 5개의 **가짜 이미지 파일을 생성**합니다.
     * - MockMultipartFile은 **스프링에서 가짜 MultipartFile을 생성**할 때 사용합니다.
     * - 각 파일의 경로는 **C:/shoppingmall_project/workspace/shop/item**로 설정됩니다.
     */
    List<MultipartFile> createMultipartFiles() throws Exception {
        List<MultipartFile> multipartFileList = new ArrayList<>(); // **빈 파일 리스트 생성**

        // 0부터 4까지 총 5개의 **MockMultipartFile** 생성
        for (int i = 0; i < 5; i++) {
            String path = "C:/shoppingmall_project/workspace/shop/item"; // **파일 경로 설정**
            String imageName = "image" + i + ".jpg"; // **이미지 파일 이름 생성** (예: image0.jpg, image1.jpg 등)

            // **MockMultipartFile 생성**
            MockMultipartFile multipartFile = new MockMultipartFile(
                    path, // **파일 경로**
                    imageName, // **파일 이름**
                    "image/jpg", // **파일 MIME 타입**
                    new byte[]{1, 2, 3, 4} // **파일의 내용 (바이트 배열)**
            );

            multipartFileList.add(multipartFile); // **리스트에 파일 추가**
        }

        return multipartFileList; // **파일 리스트 반환**
    }

    /**
     * 📘 **상품 등록 테스트 메서드**
     *
     * @throws Exception
     *
     * 🛠️ **주요 동작**
     * 1️⃣ **테스트용 상품 정보 생성**
     * - 상품 정보를 담은 **ItemFormDto 객체를 생성**합니다.
     * - 상품명, 판매 상태, 상세 설명, 가격, 재고 수량을 설정합니다.
     *
     * 2️⃣ **테스트용 이미지 파일 생성**
     * - **createMultipartFiles() 메서드를 호출**하여 5개의 **MockMultipartFile** 객체를 생성합니다.
     *
     * 3️⃣ **ItemService의 saveItem() 메서드 호출**
     * - **상품 정보와 이미지 파일을 저장**합니다.
     * - 저장된 상품의 **ID를 반환**합니다.
     *
     * 4️⃣ **저장된 이미지 정보 검증**
     * - **ItemImgRepository를 통해 이미지 정보를 조회**합니다.
     *
     * 5️⃣ **저장된 상품 정보 검증**
     * - **ItemRepository를 통해 상품 정보를 조회**합니다.
     */
    @Test
    @DisplayName("상품 등록 테스트") // **테스트 메서드의 이름을 명시**
    @WithMockUser(username = "admin", roles = "ADMIN") // **가짜 사용자 인증 (ADMIN 권한)**
    void saveItem() throws Exception {

        // 1️⃣ **테스트용 상품 정보 생성**
        ItemFormDto itemFormDto = new ItemFormDto(); // **ItemFormDto 객체 생성**
        itemFormDto.setItemNm("테스트상품"); // **상품명 설정**
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL); // **상품 판매 상태 설정**
        itemFormDto.setItemDetail("테스트 상품 입니다."); // **상품 상세 설명 설정**
        itemFormDto.setPrice(1000); // **상품 가격 설정**
        itemFormDto.setStockNumber(100); // **재고 수량 설정**

        // 2️⃣ **테스트용 이미지 파일 생성**
        List<MultipartFile> multipartFileList = createMultipartFiles(); // **5개의 가짜 파일 생성**

        // 3️⃣ **상품 정보와 이미지 파일 저장**
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList); // **ItemService의 saveItem() 호출**

        // 4️⃣ **저장된 이미지 정보 검증**
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        // **ItemImgRepository를 통해 저장된 이미지 정보 조회**

        // 5️⃣ **저장된 상품 정보 검증**
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        // **ItemRepository를 통해 저장된 상품 정보 조회**

        /**
         * 6️⃣ **저장된 상품 정보 검증**
         *
         * - **ItemFormDto의 값과 Item 엔티티의 값이 일치하는지 확인**합니다.
         * - **상품명, 판매 상태, 상세 설명, 가격, 재고 수량**이 올바르게 저장되었는지를 검증합니다.
         * - **JUnit의 assertEquals() 메서드**를 사용하여 **예상 값과 실제 값이 같은지 검증**합니다.
         */

        // **상품명 검증**: ItemFormDto의 상품명과 Item 엔티티의 상품명이 같은지 확인합니다.
        assertEquals(itemFormDto.getItemNm(), item.getItemNm());
        // **판매 상태 검증**: ItemFormDto의 판매 상태와 Item 엔티티의 판매 상태가 같은지 확인합니다.
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        // **상품 상세 설명 검증**: ItemFormDto의 상세 설명과 Item 엔티티의 상세 설명이 같은지 확인합니다.
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        // **상품 가격 검증**: ItemFormDto의 가격과 Item 엔티티의 가격이 같은지 확인합니다.
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        // **재고 수량 검증**: ItemFormDto의 재고 수량과 Item 엔티티의 재고 수량이 같은지 확인합니다.
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());

        /**
         * 7️⃣ **저장된 상품 이미지 정보 검증**
         *
         * - **multipartFileList의 원래 파일 이름과 ItemImg 엔티티의 원래 파일명이 같은지 확인**합니다.
         * - **JUnit의 assertEquals() 메서드**를 사용하여 파일명이 올바르게 저장되었는지 검증합니다.
         */
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());
        // **첫 번째 이미지 파일 이름 검증**: multipartFileList의 첫 번째 이미지 이름과 ItemImg 엔티티의 oriImgName이 같은지 확인합니다.


    }
}
