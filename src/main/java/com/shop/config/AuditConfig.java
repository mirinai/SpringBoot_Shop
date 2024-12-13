package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 📘 AuditConfig 클래스 - JPA Auditing 설정을 위한 구성 클래스
 *
 * 이 클래스는 JPA의 Auditing 기능을 활성화하고,
 * 로그인한 사용자의 정보를 등록자(createdBy), 수정자(modifiedBy)로 기록하기 위한 AuditorAware 구현체를 등록합니다.
 */
@Configuration // 📘 이 클래스가 스프링의 설정 클래스임을 나타냅니다.
@EnableJpaAuditing // 📘 JPA의 Auditing 기능을 활성화하여 @CreatedBy, @LastModifiedBy, @CreatedDate, @LastModifiedDate를 사용할 수 있도록 합니다.
public class AuditConfig {

    /**
     * 📘 AuditorAware 빈 등록 메서드
     *
     * AuditorAware 인터페이스를 구현한 AuditorAwareImpl 객체를 스프링 빈으로 등록합니다.
     * 스프링 데이터 JPA는 @CreatedBy와 @LastModifiedBy에 작성자와 수정자 정보를 자동으로 설정하기 위해
     * 이 메서드에서 반환하는 AuditorAware<String>을 사용합니다.
     *
     * @return 현재 로그인한 사용자의 이름(username)을 반환하는 AuditorAware 구현체
     */
    @Bean // 📘 AuditorAware 인터페이스 구현체를 빈으로 등록합니다.
    public AuditorAware<String> auditorProvider(){
        return new AuditorAwareImpl(); // 📘 로그인한 사용자의 정보를 제공하는 AuditorAwareImpl 인스턴스를 반환합니다.
    }
}
