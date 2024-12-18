package com.shop.service;

import com.shop.entity.ItemImg; // ItemImg 엔티티 import
import com.shop.repository.ItemImgRepository; // ItemImgRepository 리포지토리 import
import lombok.RequiredArgsConstructor; // Lombok의 @RequiredArgsConstructor import (final 필드를 포함한 생성자 자동 생성)
import org.springframework.beans.factory.annotation.Value; // @Value 어노테이션 import (application.properties의 프로퍼티를 불러오기 위해 사용)
import org.springframework.stereotype.Service; // @Service 어노테이션 import (서비스 클래스임을 명시)
import org.springframework.transaction.annotation.Transactional; // @Transactional import (트랜잭션 처리)
import org.springframework.web.multipart.MultipartFile; // 업로드된 파일을 다루기 위한 MultipartFile import
import org.thymeleaf.util.StringUtils; // 빈 문자열 확인을 위해 사용되는 유틸리티 import

/**
 * 📘 **ItemImgService 클래스**
 *
 * 이 클래스는 **상품 이미지의 업로드와 저장 기능을 처리하는 서비스 클래스**입니다.
 *
 * 🛠️ **주요 역할**
 * - **상품 이미지 업로드 처리**
 * - **파일 정보 및 URL 저장**
 * - **파일 경로와 URL 매핑**
 *
 * 🛠️ **주요 메서드**
 * - **saveItemImg()**: 이미지 파일을 업로드하고 ItemImg 정보를 데이터베이스에 저장합니다.
 */
@Service // 스프링 컨테이너에 서비스 빈으로 등록
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성
@Transactional // **모든 메서드에 트랜잭션을 적용**하여 데이터 정합성을 보장
public class ItemImgService {

    /**
     * 📘 **업로드 경로 (itemImgLocation)**
     *
     * application.properties에 정의된 `itemImgLocation` 값을 불러옵니다.
     * 예: `itemImgLocation=C:/shoppingmall_project/workspace/shop/item/`
     *
     * 이 경로에 파일이 저장되며, **상품 이미지의 물리적 경로**로 사용됩니다.
     */
    @Value("${itemImgLocation}") // application.properties에 정의된 itemImgLocation 프로퍼티 값을 불러옵니다.
    private String itemImgLocation;

    /**
     * 📘 **ItemImgRepository**
     *
     * **ItemImg 테이블에 접근하기 위한 리포지토리**로, 상품 이미지 정보를 데이터베이스에 저장하거나 조회할 때 사용합니다.
     */
    private final ItemImgRepository itemImgRepository; // 리포지토리 DI (의존성 주입)

    /**
     * 📘 **FileService**
     *
     * **파일 업로드 및 파일 삭제 기능을 담당하는 서비스**입니다.
     * 이 서비스는 **실제 파일을 저장하고 삭제하는 로직**을 담당합니다.
     */
    private final FileService fileService; // 파일 업로드 및 삭제 서비스 DI (의존성 주입)

    /**
     * 📘 **상품 이미지 저장 메서드 (saveItemImg)**
     *
     * @param itemImg ItemImg 엔티티 - **상품 이미지 정보 객체**
     * @param itemImgFile MultipartFile - **업로드된 이미지 파일**
     * @throws Exception 업로드 및 파일 저장 중 예외가 발생할 수 있습니다.
     *
     * 🛠️ **주요 동작**
     * 1️⃣ **이미지 파일 업로드**
     *   - 파일이 비어 있지 않다면, **파일을 서버에 업로드**합니다.
     *   - 업로드한 파일의 이름은 **UUID + 확장자**로 자동 생성됩니다.
     *   - 파일은 **itemImgLocation 경로에 저장**됩니다.
     *
     * 2️⃣ **URL 생성**
     *   - 업로드한 이미지의 URL 경로를 생성합니다.
     *   - 예: /images/item/파일명.jpg
     *
     * 3️⃣ **ItemImg 엔티티 정보 업데이트**
     *   - ItemImg 엔티티에 **원래 파일명, 저장된 파일명, 이미지 URL**을 저장합니다.
     *
     * 4️⃣ **데이터베이스에 이미지 정보 저장**
     *   - ItemImg 엔티티의 정보를 **데이터베이스에 저장**합니다.
     */
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {

        // 1️⃣ **원래 파일명 추출**: 업로드된 파일의 원래 파일명을 가져옵니다.
        String oriImgName = itemImgFile.getOriginalFilename(); // 예: "example.jpg"

        // 2️⃣ **저장된 파일명 및 URL 초기화**:
        // 파일이 없는 경우에는 빈 문자열로 초기화합니다.
        String imgName = ""; // 업로드된 파일의 고유 이름 (UUID + 확장자)
        String imgUrl = "";  // 브라우저에서 접근 가능한 URL 경로


        // 3️⃣ **이미지 파일 업로드**
        //
        //  - 파일이 비어 있지 않은 경우, 파일을 업로드합니다.
        //  - 업로드된 파일의 이름을 **UUID로 고유하게 생성**합니다.
        //  - 물리적 파일 경로에 **itemImgLocation 경로 + UUID 파일명**으로 저장됩니다.
        //  - 파일 저장 후, URL 경로를 **/images/item/파일명.jpg**로 설정합니다.

        if(!StringUtils.isEmpty(oriImgName)) { // 파일이 존재할 경우에만 실행

            // **파일 업로드**: FileService를 사용하여 파일을 업로드합니다.
            // - itemImgLocation: 파일이 저장될 경로 (C:/shoppingmall_project/workspace/shop/item/)
            // - oriImgName: 원래 파일명 (example.jpg)
            // - itemImgFile.getBytes(): 파일 데이터 (byte[])
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());

            // **URL 생성**: 브라우저가 이미지를 접근할 수 있는 URL 경로 생성
            // 예: /images/item/uuid.jpg
            imgUrl = "/images/item/" + imgName;
        }

        /**
         * 4️⃣ **ItemImg 엔티티 정보 업데이트**
         *
         * - 상품 이미지 엔티티(ItemImg)에 **원래 파일명, 저장된 파일명, 이미지 URL**을 저장합니다.
         * - ItemImg 엔티티의 **updateItemImg() 메서드**를 호출하여 엔티티의 상태를 업데이트합니다.
         */
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);

        /**
         * 5️⃣ **데이터베이스에 상품 이미지 정보 저장**
         *
         * - itemImgRepository의 save() 메서드를 호출하여 **상품 이미지 정보를 데이터베이스에 저장**합니다.
         */
        itemImgRepository.save(itemImg); // 데이터베이스에 저장
    }
}
