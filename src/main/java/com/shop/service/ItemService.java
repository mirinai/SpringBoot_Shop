package com.shop.service;

import com.shop.dto.ItemFormDto; // 상품 등록 폼 데이터를 담는 DTO
import com.shop.dto.ItemImgDto; // 상품 이미지 정보를 담는 DTO
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item; // Item 엔티티 (상품 엔티티)
import com.shop.entity.ItemImg; // ItemImg 엔티티 (상품 이미지 엔티티)
import com.shop.repository.ItemImgRepository; // 상품 이미지 리포지토리
import com.shop.repository.ItemRepository; // 상품 리포지토리
import jakarta.persistence.EntityNotFoundException; // 엔티티를 찾을 수 없는 경우 발생하는 예외
import lombok.RequiredArgsConstructor; // final 필드에 대한 생성자를 자동으로 생성
import org.springframework.beans.factory.annotation.Value; // application.properties에 정의된 값을 주입받기 위해 사용
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service; // 스프링의 서비스 어노테이션
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 관리
import org.springframework.util.StringUtils; // 문자열 유틸리티
import org.springframework.web.multipart.MultipartFile; // 파일 업로드 시 사용하는 클래스

import java.util.ArrayList; // 리스트 생성
import java.util.List; // 리스트 인터페이스

/**
 * 📘 **ItemService 클래스**
 *
 * 🛠️ **주요 역할**
 * - 상품 등록, 수정, 조회 관련 기능을 제공
 * - 상품 이미지의 등록 및 수정 기능 포함
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository; // 상품 정보를 다루는 리포지토리
    private final ItemImgService itemImgService; // 상품 이미지 저장 관련 서비스
    private final ItemImgRepository itemImgRepository; // 상품 이미지 정보를 다루는 리포지토리
    private final FileService fileService; // 파일 관련 서비스 (파일 삭제, 업로드)



    /**
     * 📘 **상품 저장 메서드 (saveItem)**
     *
     * @param itemFormDto 상품 등록 폼에서 전달된 상품 정보
     * @param itemImgFileList 업로드된 이미지 파일 리스트
     * @return 저장된 상품의 ID
     */
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        // 상품 정보 저장
        Item item = itemFormDto.createItem(); // 상품 정보 생성
        itemRepository.save(item); // 상품 정보 저장

        // 상품 이미지 정보 저장
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg(); // 상품 이미지 엔티티 생성
            itemImg.setItem(item); // 상품과 연관 관계 설정
            itemImg.setRepImgYn(i == 0 ? "Y" : "N"); // 첫 번째 이미지를 대표 이미지로 설정
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 이미지 파일 저장
        }

        return item.getId(); // 상품 ID 반환
    }

    /**
     * 📘 **상품 상세 정보 조회 메서드 (getItemDtl)**
     *
     * @param itemId 조회할 상품의 ID
     * @return 상품의 상세 정보 (ItemFormDto 객체)
     */
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {
        // 상품 이미지 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 이미지 정보를 담을 리스트 생성

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg); // 엔티티를 DTO로 변환
            itemImgDtoList.add(itemImgDto); // 리스트에 추가
        }

        // 상품 정보 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new); // 상품이 없을 경우 예외 발생

        // 상품 정보를 DTO로 변환
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList); // 상품 이미지 리스트 추가

        return itemFormDto; // 상품 상세 정보 반환
    }



    /**
     * 📘 **상품 정보 수정 메서드 (updateItem)**
     *
     * @param itemFormDto **수정할 상품 정보 DTO**
     * @param itemImgFileList **수정할 이미지 파일 리스트**
     * @return **수정된 상품의 ID**
     */
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 1️⃣ **상품 정보 수정**
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new); // 상품 조회, 없으면 예외 발생
        item.updateItem(itemFormDto); // 상품 정보 업데이트

        // 2️⃣ **상품 이미지 수정**
        List<Long> itemImgIds = itemFormDto.getItemImgIds(); // 이미지 ID 리스트 가져오기
        for (int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i)); // 이미지 수정
        }

        // 3️⃣ **수정된 상품의 ID 반환**
        return item.getId();
    }

    /**
     * 📘 **관리자 상품 페이지 조회 메서드 (getAdminItemPage)**
     *
     * @param itemSearchDto 상품 검색 조건을 담은 DTO
     * @param pageable 페이징 정보를 담은 객체 (페이지 번호, 크기 등)
     * @return 조건에 맞는 상품 데이터를 페이징 처리하여 반환 (Page<Item> 객체)
     */
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션으로 설정 (성능 최적화)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        // ItemRepository에서 Querydsl로 구현된 getAdminItemPage 메서드를 호출
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

}
