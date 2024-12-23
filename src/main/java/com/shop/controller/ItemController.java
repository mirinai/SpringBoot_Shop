package com.shop.controller;

import com.shop.dto.ItemFormDto; // **상품 등록/수정 시 사용하는 DTO** (Data Transfer Object)
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService; // **상품 등록 서비스** (ItemService) 임포트
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid; // **유효성 검사를 위해 사용**하는 @Valid 어노테이션
import lombok.RequiredArgsConstructor; // **final 필드에 생성자를 자동으로 추가**해주는 Lombok의 어노테이션
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller; // **Spring MVC의 컨트롤러**로 등록하는 어노테이션
import org.springframework.ui.Model; // **뷰(View)로 데이터를 전달**하기 위해 사용되는 객체
import org.springframework.validation.BindingResult; // **유효성 검사 결과를 담는 객체**
import org.springframework.web.bind.annotation.GetMapping; // **GET 요청을 처리**하기 위한 어노테이션
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // **POST 요청을 처리**하기 위한 어노테이션
import org.springframework.web.bind.annotation.RequestParam; // **요청 파라미터를 매핑**하는 어노테이션
import org.springframework.web.multipart.MultipartFile; // **파일 업로드**를 위해 사용하는 객체


import java.util.List; // **이미지 파일 리스트**를 다루기 위해 사용하는 자바의 List 인터페이스
import java.util.Optional;

/**
 * 📘 **ItemController 클래스**
 *
 * 이 컨트롤러는 **상품 등록과 관련된 모든 요청을 처리**하는 역할을 합니다.
 *
 * 🛠️ **주요 역할**
 * - **상품 등록 폼 표시** (GET 요청)
 * - **상품 등록 요청 처리** (POST 요청)
 */
@Controller // **Spring MVC 컨트롤러로 등록** (사용자의 요청을 처리하는 클래스)
@RequiredArgsConstructor // **final로 선언된 필드의 생성자를 자동으로 생성**합니다.
public class ItemController {

    /**
     * 📘 **ItemService**
     *
     * **상품 등록 서비스**로, 상품과 상품 이미지의 정보를 저장하는 로직이 담겨 있습니다.
     */
    private final ItemService itemService; // **ItemService의 의존성을 주입**합니다.


    //📘 **상품 등록 폼 (GET 요청)**
    //
    //@param model **뷰(View)로 데이터를 전달**하기 위해 사용하는 객체
    //@return **/item/itemForm 뷰 파일의 경로**

    //🛠️ **주요 동작**1️⃣ **빈 ItemFormDto 객체 생성**: 새로운 상품 정보를 입력할 때 사용합니다.
    //2️⃣ **뷰에 전달**: 뷰(View)로 **ItemFormDto 객체를 전달**하여 상품 등록 폼을 생성합니다.

    @GetMapping(value = "/admin/item/new") // **/admin/item/new** URL의 GET 요청을 처리합니다.
    public String itemForm(Model model) {
        // **빈 ItemFormDto 객체 생성**: 신규 상품 등록을 위해 빈 객체를 생성합니다.
        model.addAttribute("itemFormDto", new ItemFormDto());

        // **뷰로 이동**: /item/itemForm 페이지로 이동합니다.
        return "/item/itemForm";
    }

    /**
     * 📘 **상품 등록 요청 처리 (POST 요청)**
     *
     * @param itemFormDto **상품 등록 폼으로부터 전달받은 상품 정보**
     * @param bindingResult **유효성 검사 결과 객체**
     * @param model **뷰(View)로 데이터를 전달**하기 위해 사용하는 객체
     * @param itemImgFileList **업로드된 상품 이미지 파일 리스트**
     * @return **상품 등록 성공 시, 메인 페이지로 리다이렉트합니다.**
     *
     * 🛠️ **주요 동작**
     * 1️⃣ **폼 유효성 검사**
     *  - @Valid 어노테이션으로 **ItemFormDto의 필드 유효성을 검사**합니다.
     *  - **유효성 검사에 실패하면 에러 메시지를 뷰에 전달**하고 **itemForm 페이지로 다시 이동**합니다.
     *
     * 2️⃣ **첫 번째 이미지 파일 필수 체크**
     *  - **첫 번째 이미지가 비어 있으면** 에러 메시지를 출력하고 다시 폼으로 이동합니다.
     *
     * 3️⃣ **상품 등록 서비스 호출**
     *  - **itemService.saveItem() 메서드를 호출**하여 상품 정보를 데이터베이스에 저장합니다.
     *
     * 4️⃣ **예외 처리**
     *  - 예외가 발생하면 **에러 메시지를 출력**하고 다시 폼으로 이동합니다.
     */
    @PostMapping(value = "/admin/item/new") // **/admin/item/new** URL의 POST 요청을 처리합니다.
    public String itemNew(
            @Valid ItemFormDto itemFormDto, // **유효성 검사를 적용**한 상품 등록 폼의 데이터
            BindingResult bindingResult, // **유효성 검사 결과**를 담는 객체
            Model model, // **뷰로 데이터를 전달**하기 위한 객체
            @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList // **업로드된 상품 이미지 파일 리스트**
    ) {

        /**
         * 1️⃣ **유효성 검사**
         *
         * - @Valid를 통해 **ItemFormDto의 필드를 검증**합니다.
         * - **bindingResult.hasErrors()**: 유효성 검사에 실패했는지 확인합니다.
         * - 유효성 검사가 실패하면 **item/itemForm 뷰로 다시 이동**합니다.
         */
        if (bindingResult.hasErrors()) {
            return "item/itemForm"; // **유효성 검사 실패 시, 다시 폼으로 이동**
        }

        /**
         * 2️⃣ **첫 번째 이미지 파일 필수 체크**
         *
         * - **첫 번째 이미지가 비어 있으면** 에러 메시지를 출력하고 다시 폼으로 이동합니다.
         */
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값입니다."); // **에러 메시지 전달**
            return "item/itemForm"; // **폼으로 다시 이동**
        }

        try {
            /**
             * 3️⃣ **상품 등록 서비스 호출**
             *
             * - **상품 정보를 저장**합니다.
             * - itemFormDto와 itemImgFileList를 매개변수로 전달하여 **ItemService.saveItem()**을 호출합니다.
             */
            itemService.saveItem(itemFormDto, itemImgFileList); // **상품 등록 처리**
        } catch (Exception e) {
            /**
             * 4️⃣ **예외 처리**
             *
             * - 예외 발생 시, **에러 메시지를 뷰에 전달**하고 **item/itemForm 뷰로 다시 이동**합니다.
             */
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다."); // **에러 메시지 전달**
            return "item/itemForm"; // **폼으로 다시 이동**
        }

        // **메인 페이지로 리다이렉트**: 등록 성공 시, 메인 페이지로 이동합니다.
        return "redirect:/";
    }

    /**
     * 📘 **상품 상세 정보 조회 메서드**
     *
     * @param itemId **URL 경로에서 전달된 상품 ID**
     * @param model **뷰(View)로 데이터를 전달하기 위한 객체**
     * @return **item/itemForm 뷰 페이지로 이동**
     */
    @GetMapping(value = "/admin/item/{itemId}") // URL 경로에 있는 {itemId}를 매핑
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId); // 상품 정보 조회
            model.addAttribute("itemFormDto", itemFormDto); // 뷰에 상품 정보 전달
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "없는 상품입니다."); // 에러 메시지 전달
            model.addAttribute("itemFormDto", new ItemFormDto()); // 빈 폼 데이터 전달
            return "item/itemForm"; // 상품 등록/수정 폼으로 이동
        }
        return "item/itemForm"; // 상품 등록/수정 폼으로 이동
    }

    /**
     * 📘 **상품 수정 메서드 (itemUpdate)**
     *
     * @param itemFormDto **수정할 상품 정보 DTO**
     * @param bindingResult **유효성 검사 결과**
     * @param itemImgFileList **수정할 이미지 파일 리스트**
     * @param model **뷰에 데이터 전달 객체**
     * @return **수정 완료 후 메인 페이지 리다이렉트**
     */
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto,
                             BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             Model model) {

        if (bindingResult.hasErrors()) { // 유효성 검사 실패 시 폼으로 이동
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm"; // 첫 번째 이미지가 없을 때 폼으로 이동
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList); // 상품 정보 및 이미지 수정
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 문제가 일어났습니다.");
            return "item/itemForm"; // 예외 발생 시 폼으로 이동
        }

        return "redirect:/"; // 수정 완료 후 메인 페이지로 리다이렉트
    }

    /**
     * 관리자 상품 관리 페이지 컨트롤러 메서드
     *
     * 역할:
     * - 상품 검색 조건과 페이징 정보를 기반으로 상품 목록을 조회하여 뷰로 전달
     * - 페이지 번호가 주어지지 않으면 기본값(0 페이지)으로 처리
     *
     * @param itemSearchDto 검색 조건을 담은 DTO 객체
     * @param page 페이지 번호 (없을 경우 기본값 0)
     * @param model 뷰에 데이터를 전달하는 객체
     * @return 상품 관리 페이지 템플릿 이름 ("item/itemMng")
     */
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto,
                             @PathVariable(value = "page", required = false) Integer page,
                             Model model) {
        // 1️⃣ 페이징 정보 생성: page가 null이면 0 페이지를 기본값으로 설정, 페이지 크기는 3
        Pageable pageable = PageRequest.of(page != null ? page : 0, 3);

        // 2️⃣ 상품 목록 조회: 검색 조건과 페이징 정보를 이용해 상품 목록 조회
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        // 3️⃣ 뷰에 데이터 전달
        model.addAttribute("items", items); // 조회된 상품 목록
        model.addAttribute("itemSearchDto", itemSearchDto); // 검색 조건
        model.addAttribute("maxPage", 5); // 페이지 네비게이션 최대 표시 페이지 수

        // 4️⃣ 뷰 템플릿 경로 반환
        return "item/itemMng"; // 상품 관리 페이지 템플릿
    }



}
