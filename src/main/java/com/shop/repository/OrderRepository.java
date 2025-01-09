package com.shop.repository;

import com.shop.entity.Order;
import jakarta.persistence.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 사용자 이메일을 기준으로 해당 사용자의 주문 리스트를 페이징하여 조회하는 쿼리
    // @Query: JPQL 쿼리를 사용하여 주문 엔티티를 조회
    // :email -> 메서드 매개변수의 email 값을 바인딩하여 사용
    // "order by o.orderDate desc": 주문 날짜를 기준으로 내림차순 정렬
    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);
    // @Param: JPQL에서 사용되는 파라미터 바인딩
    // Pageable: 페이지 번호와 페이지 크기 등 페이징 정보를 포함한 객체

    // 사용자 이메일을 기준으로 해당 사용자의 총 주문 건수를 조회하는 쿼리
    // "select count(o)": JPQL에서 주문 수를 세기 위한 count 함수 사용
    @Query("select count(o) from Order o " +
            "where o.member.email = :email")
    Long countOrder(@Param("email") String email);
    // 반환 값: 해당 이메일로 등록된 총 주문 건수
}

