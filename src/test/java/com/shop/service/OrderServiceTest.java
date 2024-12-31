package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.OrderDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderServiceTest {

    @Autowired
    private OrderService orderService; // 주문 서비스 의존성 주입

    @Autowired
    private OrderRepository orderRepository; // 주문 레포지토리 의존성 주입

    @Autowired
    private ItemRepository itemRepository; // 상품 레포지토리 의존성 주입

    @Autowired
    private MemberRepository memberRepository; // 회원 레포지토리 의존성 주입

    /**
     * 테스트용 상품 생성 및 저장
     * @return 저장된 Item 엔티티 객체
     */
    public Item saveItem() {
        Item item = new Item();

        // 상품 정보 설정
        item.setItemNm("테스트 상품");
        item.setPrice(10000); // 상품 가격
        item.setItemDetail("테스트 상품 상세 설명"); // 상품 상세 설명
        item.setItemSellStatus(ItemSellStatus.SELL); // 상품 판매 상태
        item.setStockNumber(100); // 상품 재고 수량

        return itemRepository.save(item); // 상품 저장
    }

    /**
     * 테스트용 회원 생성 및 저장
     * @return 저장된 Member 엔티티 객체
     */
    public Member savaMember() {
        Member member = new Member();

        // 회원 이메일 설정
        member.setEmail("test@test.com");

        return memberRepository.save(member); // 회원 저장
    }

    @Test
    @DisplayName("주문 테스트")
    public void order() {
        // 테스트를 위한 상품과 회원 저장
        Item item = saveItem(); // 테스트용 상품 생성 및 저장
        Member member = savaMember(); // 테스트용 회원 생성 및 저장

        // 주문 정보를 담을 DTO 생성
        OrderDto orderDto = new OrderDto();

        // 주문 수량과 상품 ID 설정
        orderDto.setCount(10); // 주문 수량
        orderDto.setItemId(item.getId()); // 주문할 상품의 ID

        // 주문 서비스 호출하여 주문 생성
        Long orderId = orderService.order(orderDto, member.getEmail());

        // 생성된 주문 ID로 주문 조회 (없으면 예외 발생)
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        // 주문에 포함된 주문 상품 리스트 조회
        List<OrderItem> orderItems = order.getOrderItems();

        // 기대되는 총 주문 금액 계산
        int totalPrice = orderDto.getCount() * item.getPrice(); // 주문 수량 * 상품 가격

        // 실제 주문의 총 금액과 기대 금액이 동일한지 검증
        assertEquals(totalPrice, order.getTotalPrice());
    }

}
