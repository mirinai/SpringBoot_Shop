package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service // Spring의 서비스 계층을 나타내는 어노테이션으로, 비즈니스 로직을 처리하는 클래스임을 명시
@Transactional // 메서드 실행 중 발생하는 작업을 하나의 트랜잭션으로 처리. 예외 발생 시 롤백됨
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성해주는 롬복(Lombok) 어노테이션
public class OrderService {

    private final ItemRepository itemRepository; // 상품 정보를 처리하는 레포지토리
    private final MemberRepository memberRepository; // 회원 정보를 처리하는 레포지토리
    private final OrderRepository orderRepository; // 주문 정보를 처리하는 레포지토리

    // 주문 생성 로직을 처리하는 메서드
    public Long order(OrderDto orderDto, String email) {
        // 주문 상품(Item)을 레포지토리에서 조회, 없을 경우 EntityNotFoundException 예외 발생
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        // 주문자의 이메일을 기반으로 회원(Member)을 조회
        Member member = memberRepository.findByEmail(email);

        // 주문 항목(OrderItem) 리스트를 생성
        List<OrderItem> orderItemList = new ArrayList<>();

        // 주문 항목 생성 및 수량 설정
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem); // 생성된 주문 항목을 리스트에 추가

        // 주문(Order) 객체 생성 (회원과 주문 항목 리스트를 포함)
        Order order = Order.createOrder(member, orderItemList);

        // 생성된 주문 객체를 데이터베이스에 저장
        orderRepository.save(order);

        // 저장된 주문 객체의 ID를 반환
        return order.getId();
    }
}

