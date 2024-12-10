package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 📘 스프링의 서비스 계층을 나타내는 어노테이션 (비즈니스 로직을 처리하는 클래스)
@Transactional // 📘 메서드 실행 중 예외가 발생하면 자동으로 롤백하는 트랜잭션 관리 어노테이션
@RequiredArgsConstructor // 📘 final로 선언된 필드의 생성자를 자동으로 생성 (의존성 주입을 간결하게 해줌)
public class MemberService implements UserDetailsService {

    // UserDetailsService
// 사용자 이름을 기반으로 사용자 정보를 조회하는 역할을 합니다.
// 스프링 시큐리티에서 인증 과정을 처리할 때 이 인터페이스를 구현한 서비스를 사용하여 사용자 정보를 로드

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

    /**
     * 사용자 이메일을 기반으로 사용자 정보를 로드합니다.
     *
     * @param email 사용자의 이메일 주소
     * @return UserDetails 객체로 변환된 사용자 정보
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우 예외를 던집니다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 이메일을 기반으로 Member 객체를 데이터베이스에서 조회합니다.
        Member member = memberRepository.findByEmail(email);

        // 조회된 Member가 없으면 UsernameNotFoundException 예외를 던집니다.
        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail()) // 사용자 이름으로 이메일을 설정합니다.
                .password(member.getPassword()) // 사용자 비밀번호를 설정합니다.
                .roles(member.getRole().toString()) // 사용자 권한을 설정합니다.
                .build();
         /* 전체 흐름 정리
        1️⃣ User.builder(): User.UserBuilder 객체 생성.
        2️⃣ username(member.getEmail()): 로그인에 사용할 **이메일(email)**을 username으로 설정합니다.
        3️⃣ password(member.getPassword()): 데이터베이스에 저장된 암호화된 비밀번호를 설정합니다.
        4️⃣ roles(member.getRole().toString()): **사용자 권한(Role)**을 설정합니다.
        5️⃣ build(): 위에서 설정한 username, password, roles 정보를 포함하는 UserDetails 객체를 생성합니다.
    * */
    }

}

