package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity{

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;


    /**
     * 📘 회원 생성 메서드
     * 입력된 MemberFormDto 데이터를 바탕으로 Member 객체를 생성합니다.
     * 비밀번호는 PasswordEncoder로 암호화한 후에 Member 객체에 추가합니다.
     *
     * @param memberFormDto 사용자 입력 데이터 (회원 정보)
     * @param passwordEncoder 비밀번호를 암호화하는 PasswordEncoder 객체
     * @return Member 생성된 Member 객체
     */

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){

        Member member = new Member();
        // 📘 MemberFormDto의 데이터를 Member 객체에 복사
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());

        // 📘 비밀번호 암호화 후 저장
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);

        // 📘 회원의 기본 역할을 ROLE.USER로 설정
//        member.setRole(Role.USER);

        // 📘 회원의 기본 역할을 ROLE.ADMIN로 설정
        member.setRole(Role.ADMIN);

        return member;// 생성된 Member 객체 반환
    }

}
