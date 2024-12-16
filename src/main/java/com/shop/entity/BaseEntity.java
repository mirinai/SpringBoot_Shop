package com.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 📘 BaseEntity - 공통 엔티티 클래스
 *
 * 이 클래스는 모든 엔티티에 공통으로 사용되는 속성(등록자, 수정자)을 정의합니다.
 *
 * BaseTimeEntity를 상속받아 생성 시간(regTime)과 수정 시간(updateTime)도 함께 관리하며,
 * 추가로 생성자(createdBy)와 수정자(modifiedBy) 정보도 자동으로 기록합니다.
 *
 * BaseEntity를 상속받는 모든 엔티티는 자동으로 생성 시간, 수정 시간, 생성자, 수정자를 기록합니다.
 */
@EntityListeners(value = {AuditingEntityListener.class}) // 📘 엔티티의 생성 및 수정 시 이벤트 리스너로 동작
@MappedSuperclass // 📘 부모 클래스를 테이블로 생성하지 않고, 상속받는 자식 클래스의 테이블에 속성을 추가
@Getter
public abstract class BaseEntity extends BaseTimeEntity{
    // 📘 BaseTimeEntity를 상속받아 regTime, updateTime 속성도 함께 상속합니다.

    /**
     * 📘 생성자 (작성자) - 엔티티가 처음 생성될 때의 작성자 정보가 기록됩니다.
     *
     * @CreatedBy - 엔티티가 **처음 생성될 때 로그인한 사용자 정보(작성자)를 자동으로 기록**합니다.
     * @Column(updatable = false) - **업데이트 시 이 컬럼은 변경되지 않도록 설정**합니다.
     */
    @CreatedBy
    // 📘 엔티티가 처음 생성될 때 생성자(createdBy) 정보를 자동으로 기록하는 어노테이션입니다.
    @Column(updatable = false)
    // 📘 이 필드는 업데이트할 수 없습니다. 엔티티가 처음 생성될 때만 값이 설정되며, 이후에는 변경되지 않습니다.
    private String createdBy; // 📘 엔티티의 생성자 (작성자)

    /**
     * 📘 수정자 - 엔티티가 수정될 때마다 수정자의 정보가 자동으로 기록됩니다.
     *
     * @LastModifiedBy - **엔티티가 수정될 때 로그인한 사용자 정보(수정자)를 자동으로 기록**합니다.
     */
    @LastModifiedBy
    // 📘 엔티티가 수정될 때 수정자(modifiedBy) 정보를 자동으로 기록하는 어노테이션입니다.
    private String modifiedBy; // 📘 엔티티의 수정자 (수정자)
}
