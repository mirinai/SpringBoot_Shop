package com.shop.dto;

import com.shop.constant.ItemSellStatus; // 상품 판매 상태를 나타내는 Enum을 불러옵니다.
import com.shop.entity.Item; // Item 엔티티를 불러옵니다.
import jakarta.validation.constraints.NotBlank; // 필수 입력값을 검증하기 위한 @NotBlank 어노테이션을 임포트합니다.
import jakarta.validation.constraints.NotNull; // 필수 입력값을 검증하기 위한 @NotNull 어노테이션을 임포트합니다.
import lombok.Getter; // Lombok의 @Getter 어노테이션으로 모든 필드의 getter 메서드를 자동 생성합니다.
import lombok.Setter; // Lombok의 @Setter 어노테이션으로 모든 필드의 setter 메서드를 자동 생성합니다.
import org.modelmapper.ModelMapper; // ModelMapper를 임포트하여 객체 변환에 사용합니다.

import java.util.ArrayList; // ArrayList를 임포트하여 리스트를 생성합니다.
import java.util.List; // List 인터페이스를 임포트합니다.

@Getter @Setter // Lombok의 @Getter, @Setter로 getter, setter 메서드를 자동 생성합니다.
public class ItemFormDto {

    private Long id; // 상품의 고유 ID

    @NotBlank(message = "상품이름은 필수 입력값입니다.") // 상품명은 필수 입력값으로 빈 문자열이 허용되지 않습니다.
    private String itemNm; // 상품명

    @NotNull(message = "가격은 필수 입력값입니다.") // 가격은 필수 입력값으로 null 값이 허용되지 않습니다.
    private Integer price; // 상품 가격

    @NotBlank(message = "이름은 필수 입력값입니다.") // 상품 상세 설명은 필수 입력값으로 빈 문자열이 허용되지 않습니다.
    private String itemDetail; // 상품 상세 설명

    @NotNull(message = "재고는 필수 입력값입니다.") // 재고는 필수 입력값으로 null 값이 허용되지 않습니다.
    private Integer stockNumber; // 상품의 재고 수량

    private ItemSellStatus itemSellStatus; // 상품 판매 상태 (SELL, SOLD_OUT 등)

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 상품 이미지 정보를 담고 있는 리스트


    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper(); // 정적 ModelMapper 인스턴스를 생성하여 재사용합니다.

    // ItemFormDto 객체의 정보를 바탕으로 Item 엔티티를 생성하는 메서드
    public Item createItem(){
        return modelMapper.map(this, Item.class); // 현재 객체(this)의 정보를 Item 클래스로 매핑하여 새로운 Item 객체를 생성합니다.
    }

    // Item 엔티티를 ItemFormDto로 변환하는 메서드
    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class); // Item 엔티티의 정보를 ItemFormDto로 변환합니다.
    }
}