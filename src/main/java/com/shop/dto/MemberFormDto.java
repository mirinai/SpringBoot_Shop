package com.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberFormDto {

    @NotBlank(message = "이름은 반드시 입력해야 하는 값이다.")
// 📘 필드 값이 null이거나 빈 문자열("")이면 유효성 검증 실패
//    공백 문자열("   ")도 허용되지 않음
    private String name;

    @NotEmpty(message = "이메일은 반드시 입력해야함")
// 📘 필드 값이 null이거나 빈 문자열("")이면 유효성 검증 실패
//    공백 문자열("   ")은 허용되지만, 완전히 비어 있으면 안 됨
    private String email;

    @NotEmpty(message = "비밀번호는 반드시 입력해야함")
// 📘 필드 값이 null이거나 빈 문자열("")이면 유효성 검증 실패
//    공백 문자열("   ")은 허용되지만, 완전히 비어 있으면 안 됨
    private String password;

    @NotEmpty(message = "주소는 반드시 입력해야함")
// 📘 필드 값이 null이거나 빈 문자열("")이면 유효성 검증 실패
//    공백 문자열("   ")은 허용되지만, 완전히 비어 있으면 안 됨
    private String address;

}
