package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 📘 스프링 부트 기반 테스트를 수행하기 위한 어노테이션 (스프링 컨텍스트를 로드하여 테스트 실행)
@Transactional // 📘 테스트 메서드 실행 후 데이터베이스의 상태를 롤백하여, 테스트 간 데이터의 독립성을 보장
@TestPropertySource(locations = "classpath:application-test.properties")
// 📘 테스트 전용 설정 파일(application-test.properties)을 사용
class MemberServiceTest {

    @Autowired
    MemberService memberService; // 📘 테스트 대상인 MemberService를 주입받음

    @Autowired
    PasswordEncoder passwordEncoder; // 📘 비밀번호 암호화를 위해 PasswordEncoder를 주입받음

    /**
     * 📘 테스트 데이터를 생성하는 헬퍼 메서드
     * 테스트에서 사용할 Member 객체를 생성합니다.
     *
     * @return 생성된 Member 객체
     */
    public Member createMember(){
        // 📘 테스트용 MemberFormDto 객체를 생성하고 값을 설정
        MemberFormDto memberFormDto = new MemberFormDto();

        memberFormDto.setEmail("test@email.com"); // 이메일 설정
        memberFormDto.setName("snezinka");       // 이름 설정
        memberFormDto.setAddress("Moskva");     // 주소 설정
        memberFormDto.setPassword("1111");      // 비밀번호 설정

        // 📘 MemberFormDto와 PasswordEncoder를 이용해 Member 엔티티를 생성
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test // 📘 JUnit5에서 테스트 메서드임을 나타내는 어노테이션
    @DisplayName("sign up test") // 📘 테스트의 가독성을 높이기 위해 테스트 이름을 지정
    public void saveMemberTest() {

        // 1️⃣ Given: 테스트 데이터를 생성
        Member member = createMember(); // 📘 테스트 데이터로 사용할 Member 객체를 생성

        // 2️⃣ When: MemberService의 saveMember 메서드를 호출
        Member savedMember = memberService.saveMember(member); // 📘 회원 정보를 저장하고 반환된 회원 데이터를 저장

        // 3️⃣ Then: 저장된 데이터와 원본 데이터가 같은지 검증
        assertEquals(member.getEmail(), savedMember.getEmail()); // 📘 이메일 검증
        assertEquals(member.getName(), savedMember.getName());   // 📘 이름 검증
        assertEquals(member.getAddress(), savedMember.getAddress()); // 📘 주소 검증
        assertEquals(member.getPassword(), savedMember.getPassword()); // 📘 비밀번호 검증
        assertEquals(member.getRole(), savedMember.getRole());   // 📘 역할(Role) 검증
    }

    @Test // 📘 JUnit5에서 테스트 메서드임을 나타내는 어노테이션
    @DisplayName("중복된 회원 가입 테스트") // 📘 테스트의 가독성을 높이기 위해 테스트 이름을 지정
    public void saveDuplicateMemberTest() {

        // 1️⃣ Given: 테스트 데이터를 생성
        Member member1 = createMember(); // 📘 첫 번째 테스트 회원 생성
        Member member2 = createMember(); // 📘 두 번째 테스트 회원 생성 (중복 회원)

        // 2️⃣ When: 첫 번째 회원을 저장
        memberService.saveMember(member1); // 📘 정상적으로 회원1을 저장

        // 3️⃣ Then: 두 번째 회원 저장 시 중복 예외 발생 검증
        // 📘 memberService.saveMember(member2)를 호출했을 때, IllegalStateException 예외가 발생해야 함
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2); // 📘 중복 회원 저장 시 예외 발생 테스트
        });

        // 📘 예외 메시지가 "이미 가입된 회원임"인지 확인
        assertEquals("이미 가입된 회원임", e.getMessage());
    }

}