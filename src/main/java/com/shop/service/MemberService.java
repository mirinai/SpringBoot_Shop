package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 📘 스프링의 서비스 계층을 나타내는 어노테이션 (비즈니스 로직을 처리하는 클래스)
@Transactional // 📘 메서드 실행 중 예외가 발생하면 자동으로 롤백하는 트랜잭션 관리 어노테이션
@RequiredArgsConstructor // 📘 final로 선언된 필드의 생성자를 자동으로 생성 (의존성 주입을 간결하게 해줌)
public class MemberService {

    private final MemberRepository memberRepository; // 📘 의존성 주입된 MemberRepository (데이터베이스에 접근하는 인터페이스)

    /**
     * 📘 회원을 저장하는 메서드
     *
     * @param member 저장할 회원 엔티티
     * @return 저장된 회원 엔티티
     */
    public Member saveMember(Member member){
        // 📘 중복된 회원이 있는지 확인 (중복 회원 검증 로직 호출)
        validateDuplicateMember(member);

        // 📘 회원을 데이터베이스에 저장 (JPA의 save() 메서드 사용)
        return memberRepository.save(member);
    }

    /**
     * 📘 중복 회원을 검증하는 메서드
     *
     * @param member 중복 검사를 수행할 회원 엔티티
     * @throws IllegalStateException 중복 회원이 존재할 경우 예외를 발생시킴
     */
    private void validateDuplicateMember(Member member){
        // 📘 이메일로 기존 회원을 조회 (MemberRepository에서 이메일로 회원을 찾음)
        Member findMember = memberRepository.findByEmail(member.getEmail());

        // 📘 중복 회원이 존재할 경우 예외 발생
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원임"); // 📘 중복 회원이 발견되면 예외를 발생시킴
        }
    }
}

