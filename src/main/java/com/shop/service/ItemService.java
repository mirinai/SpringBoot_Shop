package com.shop.service;

import com.shop.dto.ItemFormDto; // ItemFormDto import (상품 등록 폼 데이터를 전달하는 DTO)
import com.shop.dto.ItemImgDto;
import com.shop.entity.Item; // Item 엔티티 import (상품 엔티티)
import com.shop.entity.ItemImg; // ItemImg 엔티티 import (상품 이미지 엔티티)
import com.shop.repository.ItemImgRepository; // ItemImgRepository import (상품 이미지 리포지토리)
import com.shop.repository.ItemRepository; // ItemRepository import (상품 리포지토리)
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor; // Lombok의 @RequiredArgsConstructor import (final 필드를 포함한 생성자 자동 생성)
import org.springframework.stereotype.Service; // 스프링의 @Service 어노테이션 import (서비스 클래스임을 명시)
import org.springframework.transaction.annotation.Transactional; // 스프링의 @Transactional 어노테이션 import (트랜잭션 처리)
import org.springframework.web.multipart.MultipartFile; // 스프링의 MultipartFile import (파일 업로드를 다루기 위한 클래스)

import java.util.ArrayList;
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

    /**
     * 📘 **상품 상세 정보 조회 메서드 (getItemDtl)**
     *
     * @param itemId 조회할 상품의 ID
     * @return 상품의 상세 정보(ItemFormDto 객체)
     *
     * 🛠️ **주요 동작**
     * 1️⃣ **상품 이미지 조회**:
     *   - 상품 ID를 기준으로 **ItemImg 테이블**에서 이미지를 조회.
     *   - 조회한 이미지를 ItemImgDto 리스트로 변환.
     *
     * 2️⃣ **상품 정보 조회**:
     *   - 상품 ID를 기준으로 **Item 테이블**에서 상품 정보를 조회.
     *
     * 3️⃣ **상품과 이미지 정보를 통합**:
     *   - Item 엔티티 정보를 ItemFormDto로 변환.
     *   - 이미지 리스트(ItemImgDto)를 ItemFormDto에 추가.
     *
     * 4️⃣ **결과 반환**:
     *   - 완성된 ItemFormDto 객체 반환.
     */
    @Transactional(readOnly = true) // **읽기 전용 트랜잭션**으로 설정: 데이터를 변경하지 않으므로 성능 최적화.
    public ItemFormDto getItemDtl(Long itemId) {

        /**
         * 1️⃣ **상품 이미지 조회**
         * - ItemImgRepository의 findByItemIdOrderByIdAsc 메서드를 호출하여,
         *   **상품 ID를 기준으로 상품 이미지 정보를 조회**합니다.
         * - **정렬 기준**: ID 오름차순(ASC).
         */
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        /**
         * 2️⃣ **ItemImgDto 리스트 생성**
         * - 조회한 ItemImg 엔티티 리스트를 기반으로 ItemImgDto 리스트를 생성합니다.
         */
        List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // **ItemImgDto 리스트**를 생성합니다.

        for (ItemImg itemImg : itemImgList) { // 조회한 ItemImg 리스트를 순회합니다.

            /**
             * - ItemImg 엔티티를 ItemImgDto로 변환합니다.
             * - of 메서드는 ItemImg 엔티티 데이터를 ItemImgDto 객체로 변환하는 정적 메서드입니다.
             */
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto); // 변환된 ItemImgDto 객체를 리스트에 추가합니다.
        }

        /**
         * 3️⃣ **상품 정보 조회**
         * - ItemRepository의 findById 메서드를 호출하여,
         *   **상품 ID를 기준으로 Item 엔티티를 조회**합니다.
         * - 조회 결과가 없으면 EntityNotFoundException 예외를 발생시킵니다.
         */
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new); // **엔티티가 없을 경우 예외 발생.**

        /**
         * 4️⃣ **ItemFormDto 생성**
         *
         * - 조회한 Item 엔티티 데이터를 기반으로 ItemFormDto 객체를 생성합니다.
         * - of 메서드는 Item 엔티티 데이터를 ItemFormDto로 변환하는 정적 메서드입니다.
         */
        ItemFormDto itemFormDto = ItemFormDto.of(item);

        /**
         * 5️⃣ **상품 이미지 리스트 추가**
         * - ItemFormDto 객체에 ItemImgDto 리스트를 추가합니다.
         */
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        /**
         * 6️⃣ **결과 반환**
         * - 완성된 ItemFormDto 객체를 반환합니다.
         */
        return itemFormDto; // **상품 상세 정보 DTO를 반환.**
    }

}

