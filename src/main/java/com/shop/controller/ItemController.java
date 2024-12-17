package com.shop.controller;

import com.shop.dto.ItemFormDto; // ItemFormDto 클래스 임포트 (상품 등록/수정 시 사용하는 DTO)
import org.springframework.stereotype.Controller; // Spring MVC의 컨트롤러 어노테이션
import org.springframework.ui.Model; // 뷰(View)로 데이터를 전달하기 위한 Model 객체 임포트
import org.springframework.web.bind.annotation.GetMapping; // GET 요청을 매핑하기 위한 어노테이션

@Controller // 이 클래스가 Spring MVC의 컨트롤러임을 명시합니다.
public class ItemController {

    @GetMapping(value = "/admin/item/new") // "/admin/item/new" URL로의 GET 요청을 처리합니다.
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto()); // 뷰(View)로 새로운 ItemFormDto 객체를 전달하여 빈 폼을 생성합니다.

        return "/item/itemForm"; // "/item/itemForm" 뷰 페이지로 이동합니다.
    }
}