package com.shop.entity;

import jakarta.persistence.*; // JPA 관련 어노테이션을 사용하기 위해 임포트
import lombok.Getter; // Lombok의 Getter 어노테이션으로 모든 필드의 getter 메서드 자동 생성
import lombok.Setter; // Lombok의 Setter 어노테이션으로 모든 필드의 setter 메서드 자동 생성

@Entity // JPA의 Entity 어노테이션으로 해당 클래스가 엔티티임을 명시
@Table(name = "item_img") // 데이터베이스에 매핑될 테이블 이름을 "item_img"로 지정
@Getter @Setter // Lombok을 사용하여 getter, setter 메서드 자동 생성
public class ItemImg extends BaseEntity{ // BaseEntity를 상속하여 생성, 수정 시간 관련 필드를 상속받음

    @Id // 기본 키(PK)로 지정
    @Column(name = "item_img_id") // 매핑할 데이터베이스의 컬럼 이름을 "item_img_id"로 지정
    @GeneratedValue(strategy = GenerationType.AUTO) // 기본 키의 생성 전략을 자동 생성(AUTO)으로 설정
    private Long id; // 상품 이미지의 고유 ID

    private String imgName; // 이미지 파일 이름 (서버에 저장될 이미지 파일 이름)

    private String oriImgName; // 원본 이미지 파일 이름 (사용자가 업로드한 원래 파일 이름)

    private String imgUrl; // 이미지 조회 경로 (이미지를 조회할 수 있는 URL 경로)

    private String repImgYn; // 대표 이미지 여부 (Y/N으로 대표 이미지를 표시)

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계로 설정, 지연 로딩 사용 (필요할 때만 로딩)
    @JoinColumn(name = "item_id") // 외래 키(FK) 컬럼 이름을 "item_id"로 지정
    private Item item; // 상품(Item) 엔티티와의 연관 관계 설정 (하나의 상품에 여러 이미지가 연결될 수 있음)

    // 상품 이미지 정보를 업데이트하는 메서드 (원본 파일명, 저장된 파일명, 이미지 URL을 업데이트)
    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName; // 원본 이미지 파일 이름 업데이트
        this.imgName = imgName; // 저장된 이미지 파일 이름 업데이트
        this.imgUrl = imgUrl; // 이미지 조회 경로(URL) 업데이트
    }
}
