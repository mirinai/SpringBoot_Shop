package com.shop.dto;

import com.shop.entity.ItemImg; // ItemImg 엔티티를 불러옵니다.
import lombok.Getter; // Lombok의 @Getter 어노테이션으로 모든 필드의 getter 메서드를 자동 생성합니다.
import lombok.Setter; // Lombok의 @Setter 어노테이션으로 모든 필드의 setter 메서드를 자동 생성합니다.
import org.modelmapper.ModelMapper; // ModelMapper를 임포트하여 객체 변환에 사용합니다.

@Getter @Setter // Lombok의 @Getter, @Setter로 getter, setter 메서드를 자동 생성합니다.
public class ItemImgDto {

    private Long id; // 이미지의 고유 ID

    private String imgName; // 서버에 저장된 이미지 파일명

    private String oriImgName; // 사용자가 업로드한 원본 이미지 파일명

    private String imgUrl; // 이미지를 조회할 수 있는 URL 경로

    private String repImgYn; // 대표 이미지 여부 (Y: 대표 이미지, N: 일반 이미지)

    private static ModelMapper modelMapper = new ModelMapper(); // 정적 ModelMapper 인스턴스를 생성하여 재사용합니다.
    // 멤버변수로 ModelMapper 객체를 넣음

    // ItemImg 엔티티를 ItemImgDto로 변환하는 메서드
    public static ItemImgDto of(ItemImg itemImg){// static 메서드로 선언해 ItemImgDto객체를 만들지 않아도 불러낼 수 있음
        return modelMapper.map(itemImg, ItemImgDto.class); // ModelMapper를 사용하여 ItemImg 엔티티를 ItemImgDto로 변환합니다.
    }
}
