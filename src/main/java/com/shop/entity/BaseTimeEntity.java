package com.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 📘 BaseTimeEntity - 엔티티의 생성 시간(regTime)과 수정 시간(updateTime)을 자동으로 기록하는 클래스
 *
 * 이 클래스는 엔티티의 공통 속성을 정의합니다.
 *
 * 엔티티의 생성 시간과 수정 시간을 자동으로 기록하며,
 * 모든 엔티티에서 중복되는 regTime, updateTime 속성을 한 곳에 모아 관리합니다.
 *
 * BaseTimeEntity를 상속받는 모든 엔티티는 자동으로 생성 시간과 수정 시간을 기록합니다.
 */
@EntityListeners(value = {AuditingEntityListener.class})
// 📘 엔티티의 변경 사항을 감지하는 리스너를 추가합니다.
// 엔티티가 생성되거나 수정될 때 @CreatedDate와 @LastModifiedDate가 자동으로 실행됩니다.
@MappedSuperclass
// 📘 MappedSuperclass는 이 클래스를 단독으로 테이블에 매핑하지 않고,
// 이를 상속받는 자식 엔티티가 이 클래스의 필드를 테이블의 컬럼으로 사용할 수 있도록 합니다.
@Getter @Setter
// 📘 롬복(Lombok) 어노테이션으로, @Getter와 @Setter를 사용하여
// 모든 필드에 대한 getter와 setter 메서드를 자동 생성합니다.
public class BaseTimeEntity {

    /**
     * 📘 엔티티 생성 시간 (등록 시간) - 최초로 엔티티가 생성될 때 자동으로 기록됩니다.
     */
    @CreatedDate // 📘 엔티티가 처음 생성될 때 생성 시간(regTime)을 자동으로 기록하는 어노테이션입니다.
    @Column(updatable = false) // 📘 이 필드는 업데이트할 수 없습니다. 엔티티가 처음 생성될 때만 값이 설정되며, 이후에는 변경되지 않습니다.
    private LocalDateTime regTime; // 📘 엔티티의 등록 시간 (생성 시간)

    /**
     * 📘 엔티티 수정 시간 - 엔티티가 수정될 때마다 자동으로 기록됩니다.
     *
     * @LastModifiedDate - **엔티티가 수정될 때 자동으로 수정 시간(updateTime)을 기록**합니다.
     */
    @LastModifiedDate
    private LocalDateTime updateTime; // 📘 엔티티의 수정 시간 (수정 시간)
}
