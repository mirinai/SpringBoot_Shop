package com.shop.service;

import com.shop.dto.ItemFormDto; // ItemFormDto import (상품 등록 폼 데이터를 전달하는 DTO)
import com.shop.entity.Item; // Item 엔티티 import (상품 엔티티)
import com.shop.entity.ItemImg; // ItemImg 엔티티 import (상품 이미지 엔티티)
import com.shop.repository.ItemImgRepository; // ItemImgRepository import (상품 이미지 리포지토리)
import com.shop.repository.ItemRepository; // ItemRepository import (상품 리포지토리)
import lombok.RequiredArgsConstructor; // Lombok의 @RequiredArgsConstructor import (final 필드를 포함한 생성자 자동 생성)
import org.springframework.stereotype.Service; // 스프링의 @Service 어노테이션 import (서비스 클래스임을 명시)
import org.springframework.transaction.annotation.Transactional; // 스프링의 @Transactional 어노테이션 import (트랜잭션 처리)
import org.springframework.web.multipart.MultipartFile; // 스프링의 MultipartFile import (파일 업로드를 다루기 위한 클래스)

import java.util.List; // 자바의 List 인터페이스 import (이미지 파일 리스트를 다루기 위해 사용)

/**
 * 📘 **ItemService 클래스**
 *
 * 이 클래스는 **상품 등록 및 상품 정보 저장**을 담당하는 서비스 클래스입니다.
 *
 * 🛠️ **주요 역할**
 * - 상품 정보(Item 엔티티)와 **상품 이미지(ItemImg 엔티티) 정보 저장**
 * - **트랜잭션 처리**: 트랜잭션이 완료되지 않으면 모든 작업이 롤백됩니다.
 */
@Service // 스프링의 **서비스 빈으로 등록**되어 비즈니스 로직을 처리하는 클래스임을 명시
@Transactional // **트랜잭션 처리**를 명시하여 메서드 내 작업이 모두 성공하면 커밋, 하나라도 실패하면 롤백합니다.
@RequiredArgsConstructor // **final로 선언된 필드의 생성자를 자동으로 생성**합니다.
public class ItemService {

    /**
     * 📘 **ItemRepository**
     *
     * **Item 테이블에 접근하는 리포지토리**로, 상품 정보를 저장하거나 조회할 때 사용합니다.
     */
    private final ItemRepository itemRepository; // 상품(Item) 엔티티의 저장 및 조회를 담당하는 리포지토리

    /**
     * 📘 **ItemImgService**
     *
     * **상품 이미지 저장 및 파일 업로드를 담당하는 서비스**로, 파일을 서버에 저장하고 URL을 생성합니다.
     */
    private final ItemImgService itemImgService; // 상품 이미지의 저장과 업로드를 담당하는 서비스

    /**
     * 📘 **ItemImgRepository**
     *
     * **ItemImg 테이블에 접근하는 리포지토리**로, 상품 이미지 정보를 저장하거나 조회할 때 사용합니다.
     */
    private final ItemImgRepository itemImgRepository; // 상품 이미지(ItemImg) 엔티티의 저장 및 조회를 담당하는 리포지토리

    /**
     * 📘 **상품 저장 메서드 (saveItem)**
     *
     * @param itemFormDto 상품 등록 폼에서 전달된 **상품 정보 DTO**
     * @param itemImgFileList **업로드된 상품 이미지 파일 리스트**
     * @return 저장된 상품의 ID
     * @throws Exception 예외가 발생할 수 있습니다.
     *
     * 🛠️ **주요 동작**
     * 1️⃣ **상품 정보 저장**
     *   - **Item 엔티티를 생성**하여 item 테이블에 저장합니다.
     *
     * 2️⃣ **이미지 정보 저장**
     *   - 상품의 이미지 파일 목록을 하나씩 업로드하고 **ItemImg 엔티티를 생성**하여 저장합니다.
     *   - **첫 번째 이미지는 대표 이미지로 설정**하고, 나머지 이미지는 일반 이미지로 설정합니다.
     */
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        /**
         * 1️⃣ **상품 정보 저장**
         *
         * - **상품 폼 DTO(ItemFormDto)로부터 Item 엔티티를 생성**합니다.
         * - ItemFormDto에 있는 createItem() 메서드를 통해 **Item 엔티티를 생성**합니다.
         */
        Item item = itemFormDto.createItem(); // ItemFormDto에 정의된 메서드를 통해 **Item 엔티티를 생성**합니다.

        // **ItemRepository에 상품 정보 저장**: Item 엔티티의 정보를 item 테이블에 저장합니다.
        itemRepository.save(item); // Item 엔티티를 item 테이블에 저장합니다.

        /**
         * 2️⃣ **상품 이미지 정보 저장**
         *
         * - **이미지 파일 리스트의 크기만큼 반복문을 실행**합니다.
         * - 각 이미지 파일에 대해 **ItemImg 엔티티**를 생성하고, **ItemImgService의 saveItemImg 메서드**를 호출합니다.
         */
        for(int i = 0; i < itemImgFileList.size(); i++) {

            /**
             * 1️⃣ **ItemImg 엔티티 생성**
             *
             * - 상품 이미지 정보(ItemImg)를 저장하기 위해 새로운 **ItemImg 엔티티**를 생성합니다.
             * - 생성한 ItemImg 엔티티에 **상품(Item)과 연결**시킵니다.
             */
            ItemImg itemImg = new ItemImg(); // **새로운 상품 이미지 엔티티**를 생성합니다.

            // **상품 엔티티와 연결**: Item 엔티티와 ItemImg 엔티티를 연관시킵니다.
            // 예: 하나의 상품(Item)에는 여러 개의 상품 이미지(ItemImg)가 연결됩니다.
            itemImg.setItem(item); // 상품 이미지와 상품을 **연관 관계로 설정**합니다.

            /**
             * 2️⃣ **대표 이미지 여부 설정**
             *
             * - 첫 번째 이미지(i == 0)는 **대표 이미지로 설정**합니다.
             * - 대표 이미지 여부는 **repImgYn** 컬럼에 **"Y" 또는 "N"**으로 설정됩니다.
             */
            if(i == 0) {
                itemImg.setRepImgYn("Y"); // **첫 번째 이미지는 대표 이미지로 설정**합니다.
            } else {
                itemImg.setRepImgYn("N"); // **나머지 이미지는 대표 이미지가 아님**을 설정합니다.
            }

            /**
             * 3️⃣ **상품 이미지 저장**
             *
             * - ItemImgService의 **saveItemImg 메서드를 호출**하여 **상품 이미지 파일을 업로드**합니다.
             * - ItemImg 엔티티와 이미지 파일(MultipartFile)을 매개변수로 전달합니다.
             */
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // **상품 이미지 파일을 업로드**하고 ItemImg 엔티티 정보를 저장합니다.
        }

        /**
         * 3️⃣ **상품 ID 반환**
         *
         * - 상품의 ID를 반환합니다.
         * - 이 ID는 **item 테이블의 기본 키(PK)**입니다.
         */
        return item.getId(); // 저장된 상품의 **고유 ID**를 반환합니다.
    }
}
