package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service // 비즈니스 로직을 처리하는 서비스 클래스임을 나타내는 어노테이션
@Transactional // 트랜잭션 처리를 위해 사용됨. 메서드 실행 중 예외 발생 시 롤백 처리됨
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성하는 Lombok 어노테이션
public class OrderService {

    private final ItemRepository itemRepository; // 상품 정보를 처리하는 레포지토리
    private final MemberRepository memberRepository; // 회원 정보를 처리하는 레포지토리
    private final OrderRepository orderRepository; // 주문 정보를 처리하는 레포지토리
    private final ItemImgRepository itemImgRepository; // 상품 이미지 정보를 처리하는 레포지토리

    // **주문 생성 메서드**
    public Long order(OrderDto orderDto, String email) {
        // 상품 ID를 기반으로 상품 엔티티 조회. 없을 경우 예외 발생
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        // 이메일을 통해 회원 엔티티를 조회
        Member member = memberRepository.findByEmail(email);

        // 주문 항목 리스트 생성
        List<OrderItem> orderItemList = new ArrayList<>();

        // 주문 항목 생성 및 수량 설정
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); // 주문 상품과 수량을 설정
        orderItemList.add(orderItem); // 리스트에 주문 항목 추가

        // 주문 객체 생성 (회원 정보와 주문 항목 리스트 포함)
        Order order = Order.createOrder(member, orderItemList);

        // 주문 정보를 데이터베이스에 저장
        orderRepository.save(order);

        // 저장된 주문 객체의 ID를 반환
        return order.getId(); // 생성된 주문 ID 반환
    }

    // **주문 내역 조회 메서드 (페이징 처리)**
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션으로 설정하여 성능 향상
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        // 해당 이메일의 주문 리스트를 페이징 처리하여 조회
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email); // 해당 이메일의 전체 주문 건수 조회

        List<OrderHistDto> orderHistDtos = new ArrayList<>(); // 주문 내역을 저장할 DTO 리스트

        for (Order order : orders) {
            // 주문 엔티티를 DTO로 변환하여 저장
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems(); // 주문 항목 리스트 조회

            for (OrderItem orderItem : orderItems) {
                // 대표 이미지("Y")인 상품 이미지 조회
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");
                // 주문 항목 정보를 DTO로 변환 (상품 정보와 이미지 URL 설정)
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                // 주문 내역 DTO에 주문 항목 DTO 추가
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto); // 완성된 주문 내역 DTO를 리스트에 추가
        }

        // 주문 내역 리스트를 Page 객체로 반환 (페이징 정보를 포함)
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        // 📝 [메서드 설명]
        // - 해당 주문이 로그인한 사용자의 주문인지 검증하는 메서드
        // - 읽기 전용 트랜잭션으로 설정하여 데이터 수정 없이 성능 최적화

        // 현재 로그인한 사용자의 이메일로 회원 엔티티 조회
        Member curMember = memberRepository.findByEmail(email);

        // 주문 ID로 주문 엔티티 조회. 없을 경우 예외 발생
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        // 조회된 주문 엔티티에 저장된 회원 정보
        Member savedMember = order.getMember();

        // 현재 로그인한 사용자의 이메일과 주문 생성자의 이메일 비교
        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            // 이메일이 다르면 해당 주문을 취소할 권한이 없으므로 false 반환
            return false;
        }
        // 이메일이 같으면 true 반환
        return true;
    }

    public void cancelOrder(Long orderId) {
        // 📝 [메서드 설명]
        // - 주문을 취소하는 메서드

        // 주문 ID로 주문 엔티티 조회. 없을 경우 예외 발생
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        // 주문 취소 메서드 호출 (Order 엔티티의 상태를 'CANCEL'로 변경하고 주문 항목의 재고 복구)
        order.cancelOrder();
    }
}
