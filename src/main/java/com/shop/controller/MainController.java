package com.shop.controller;


import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    /**
     * ItemService 주입 (의존성 주입으로 서비스 계층과 연결)
     */
    private final ItemService itemService;

    /**
     * 메인 페이지 요청 처리
     * - 상품 검색 및 페이징 처리
     *
     * @param itemSearchDto 검색 조건을 담은 DTO
     * @param page 현재 페이지 번호 (기본값: 0)
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 메인 페이지 뷰 이름("main")
     */
    @GetMapping(value = "/")
    public String getMainPage(ItemSearchDto itemSearchDto,
                              @RequestParam(value = "page", defaultValue = "0") Integer page,
                              Model model){
        // 페이징 정보 생성 (현재 페이지, 페이지당 항목 수: 6개)
        Pageable pageable = PageRequest.of(page != null ? page : 0, 6);

        // 검색 조건 및 페이징 정보를 기반으로 상품 리스트 가져오기
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        // 모델에 데이터 추가
        model.addAttribute("items", items);               // 상품 리스트
        model.addAttribute("itemSearchDto", itemSearchDto); // 검색 조건
        model.addAttribute("maxPage", 5);                 // 최대 페이지 수

        // 메인 페이지 뷰 반환
        return "main";
    }
}
