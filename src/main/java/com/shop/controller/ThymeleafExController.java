package com.shop.controller;

import com.shop.dto.ItemDto;
import com.shop.entity.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf") // 모든 핸들러 메서드의 기본 경로를 "/thymeleaf"로 설정
public class ThymeleafExController {

    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model){
        model.addAttribute("data", "타임리프 예제임!!!!");
        return "thymeleafEx/thymeleafEx01";
    }

    // GET 요청으로 "/ex02" URL에 접근할 경우 실행되는 핸들러 메서드
    @GetMapping(value = "/ex02") //th:text exam
    public String thymeleafExample02(Model model) {
        // ItemDto 객체 생성
        ItemDto itemDto = new ItemDto();

        // ItemDto의 필드 값을 설정
        itemDto.setItemDetail("상품 상세 설명"); // 상품의 상세 설명 설정
        itemDto.setItemNm("테스트 상품1");       // 상품 이름 설정
        itemDto.setPrice(10000);               // 상품 가격 설정
        itemDto.setRegTime(LocalDateTime.now()); // 상품 등록 시간을 현재 시간으로 설정

        // Model 객체에 itemDto를 "itemDto"라는 이름으로 추가
        // 뷰에서 "itemDto"라는 이름으로 데이터를 참조할 수 있음
        model.addAttribute("itemDto", itemDto);

        // Thymeleaf 템플릿 파일 경로 반환
        // templates/thymeleafEx/thymeleafEx02.html 파일을 렌더링
        return "thymeleafEx/thymeleafEx02";
    }


    // "/ex03" URL로 GET 요청이 들어오면 실행되는 메서드
    @GetMapping(value = "/ex03") //th:each exam
    public String thymeleafExample03(Model model){

        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i = 1;i<=10;i++){

            // ItemDto 객체를 저장할 리스트 생성
            ItemDto itemDto = new ItemDto();

            // ItemDto 객체의 필드 값 설정
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setItemNm("테스트 상품"+i);
            itemDto.setPrice(1000*i);
            itemDto.setRegTime(LocalDateTime.now());

            // 생성한 ItemDto 객체를 리스트에 추가
            itemDtoList.add(itemDto);
        }

        // Model 객체에 itemDtoList를 "itemDtoList"라는 이름으로 추가
        // 뷰에서 "itemDtoList"를 참조하여 데이터를 출력할 수 있음
        model.addAttribute("itemDtoList", itemDtoList);

        // Thymeleaf 템플릿 파일 경로 반환
        // templates/thymeleafEx/thymeleafEx03.html 파일을 렌더링
        return "thymeleafEx/thymeleafEx03";
    }

    // "/ex04" URL로 GET 요청이 들어오면 실행되는 메서드
    @GetMapping(value = "/ex04") //th:if, th:unless, th:switch, th:case
    public String thymeleafExample04(Model model) {

        // ItemDto 객체를 저장할 리스트 생성
        List<ItemDto> itemDtoList = new ArrayList<>();

        // 1부터 10까지 반복하여 ItemDto 객체를 생성하고 리스트에 추가
        for (int i = 1; i <= 10; i++) {
            // 새로운 ItemDto 객체 생성
            ItemDto itemDto = new ItemDto();

            // ItemDto의 필드 값 설정
            itemDto.setItemDetail("상품 상세 설명" + i); // 상품 상세 설명 설정
            itemDto.setItemNm("테스트 상품" + i);        // 상품명 설정
            itemDto.setPrice(1000*i);                   // 상품 가격 설정
            itemDto.setRegTime(LocalDateTime.now());    // 현재 시간으로 등록 시간 설정

            // 생성한 ItemDto 객체를 리스트에 추가
            itemDtoList.add(itemDto);
        }

        // Model 객체에 itemDtoList를 "itemDtoList"라는 이름으로 추가
        // 뷰에서 "itemDtoList"를 참조하여 데이터를 출력할 수 있음
        model.addAttribute("itemDtoList", itemDtoList);

        // Thymeleaf 템플릿 파일 경로 반환
        // templates/thymeleafEx/thymeleafEx04.html 파일을 렌더링
        return "thymeleafEx/thymeleafEx04";
    }

    // "/thymeleaf/ex05" URL에 대한 GET 요청을 처리하는 메서드
    @GetMapping(value = "/ex05")
    public String thymeleafExample05(){
        // "thymeleafEx/thymeleafEx05.html" 템플릿을 렌더링하여 응답으로 반환
        return "thymeleafEx/thymeleafEx05";
    }

    // "/thymeleaf/ex06" URL에 대한 GET 요청을 처리하는 메서드
    @GetMapping(value = "/ex06")
    public String thymeleafExample06(String param1, String param2, Model model){ //IDE가 알아서 RequestParam이 없어도 맞으면 넣어줌
        // 요청으로부터 전달된 파라미터 "param1"과 "param2"를 모델에 추가
        model.addAttribute("param1", param1);
        model.addAttribute("param2",param2);

        // "thymeleafEx/thymeleafEx06.html" 템플릿을 렌더링하여 응답으로 반환
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping(value = "/ex07")
    public String thymeleafExample07(){
        return "thymeleafEx/thymeleafEx07";
    }
}
