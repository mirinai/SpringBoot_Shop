package com.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * 📘 현재 로그인한 사용자의 이름(username)을 반환하는 메서드
     *
     * Spring Security의 SecurityContextHolder를 통해 현재 로그인한 사용자의 인증 정보를 가져옵니다.
     * 로그인한 사용자가 없을 경우, 빈 문자열("")을 반환합니다.
     *
     * @return 로그인한 사용자의 이름(username)을 Optional로 감싸서 반환합니다.
     */
    @Override
    public Optional<String> getCurrentAuditor(){
        // 📘 현재 보안 컨텍스트(SecurityContext)에서 인증(Authentication) 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 📘 기본 값으로 빈 문자열을 할당 (로그인 정보가 없는 경우)
        String userId = "";

        // 📘 인증 정보(authentication)가 존재할 경우, 로그인한 사용자의 이름(username)을 가져옵니다.
        if(authentication != null && authentication.isAuthenticated()){
            userId = authentication.getName(); // 📘 로그인한 사용자의 이름(username)을 반환합니다.
        }

        // 📘 Optional로 userId를 감싸서 반환합니다. (null을 방지하기 위함)
        return Optional.of(userId);
    }
}
