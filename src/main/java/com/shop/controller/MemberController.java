package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/members") // 📘 URL 경로 "/members"로 들어오는 요청을 처리하는 컨트롤러
@Controller // 📘 스프링 MVC의 컨트롤러임을 나타냄 (View 반환)
@RequiredArgsConstructor // 📘 final로 선언된 필드를 생성자를 통해 자동으로 주입 (의존성 주입)
public class MemberController {

    private final MemberService memberService; // 📘 회원 관련 비즈니스 로직을 처리하는 MemberService 의존성 주입
    private final PasswordEncoder passwordEncoder; // 📘 비밀번호를 암호화하는 PasswordEncoder 인스턴스 (의존성 주입)
    // - 비밀번호를 데이터베이스에 저장하기 전에 암호화하기 위해 사용
    // - 주로 BCryptPasswordEncoder를 사용하여 해시 알고리즘으로 비밀번호를 변환
    // - final 키워드로 선언되어 불변성을 보장 (생성자를 통해 주입됨)

    /**
     * 📘 회원 가입 폼 화면을 반환하는 메서드
     *
     * @param model 뷰(View)로 데이터를 전달하기 위한 Model 객체
     * @return "member/memberForm" 뷰 이름 (회원 가입 폼 화면)
     */
    @GetMapping(value = "/new") // 📘 URL "/members/new" 요청을 처리 (GET 방식)
    public String memberForm(Model model){
        // 📘 뷰에서 사용할 MemberFormDto 객체를 Model에 추가
        model.addAttribute("memberFormDto", new MemberFormDto());

        // 📘 "member/memberForm" 뷰를 반환
        return "member/memberForm"; // 회원 가입 폼 화면
    }

    @PostMapping(value = "/new")
// 📘 HTTP POST 요청을 처리하는 메서드로, /member/new 경로로 들어오는 POST 요청을 처리

    public String memberForm(MemberFormDto memberFormDto) {
        // 📘 클라이언트가 전송한 회원가입 폼 데이터를 MemberFormDto 객체로 자동 바인딩

        Member member = Member.createMember(memberFormDto, passwordEncoder);
        // 📘 MemberFormDto의 데이터를 기반으로 Member 엔티티 생성
        // 📘 비밀번호는 PasswordEncoder를 통해 암호화하여 Member 엔티티에 설정

        memberService.saveMember(member);
        // 📘 MemberService의 saveMember() 메서드를 호출하여 Member 엔티티를 데이터베이스에 저장

        return "redirect:/";
        // 📘 회원가입이 완료되면 루트 경로("/")로 리다이렉트하여 메인 페이지로 이동
    }

}

