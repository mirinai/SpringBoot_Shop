package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

// ItemRepository 인터페이스 정의
// JpaRepository와 QuerydslPredicateExecutor, ItemRepositoryCustom 인터페이스를 상속받아
// 기본 CRUD, Querydsl을 활용한 동적 쿼리, 사용자 정의 쿼리 기능을 제공
public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    // JpaRepository<Item, Long>
    // - Spring Data JPA에서 제공하는 기본 CRUD 메서드 및 페이징 기능을 지원
    // - 제네릭 타입 Item: 엔티티 클래스
    // - 제네릭 타입 Long: 엔티티의 ID 타입

    // QuerydslPredicateExecutor<Item>
    // - Querydsl을 활용한 동적 쿼리 생성을 지원
    // - Predicate를 통해 조건을 정의하여 복잡한 쿼리 작성 가능

    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // 특정한 JPQL 쿼리를 직접 정의하는 @Query 어노테이션
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
    // 설명:
    // 1. select i from Item i
    //    - "Item"은 JPA 엔티티 클래스의 이름을 참조하며, "i"는 이 클래스의 별칭입니다.
    //    - 엔티티 클래스인 "Item"으로부터 모든 데이터를 선택(select)합니다.
    //
    // 2. where i.itemDetail like %:itemDetail%
    //    - "i.itemDetail"은 "Item" 엔티티 클래스의 "itemDetail" 필드를 참조합니다.
    //    - ":itemDetail"은 파라미터로 전달된 값을 나타냅니다.
    //    - "like %:itemDetail%"는 SQL의 LIKE 연산을 사용하여 주어진 파라미터 값이 필드에 포함되어 있는지를 찾습니다.
    //    - 앞뒤에 "%"가 있으므로, 파라미터 값이 itemDetail 필드에 **부분적으로 일치**하는 항목들을 검색합니다.
    //
    // 3. order by i.price desc
    //    - 결과를 "price" 필드를 기준으로 내림차순(descending) 정렬합니다.

    // 파라미터 정의 및 메서드 선언
    // @Param("itemDetail") String itemDetail
    // - @Param("itemDetail"): JPQL 쿼리에서 사용하는 파라미터 ":itemDetail"과 이 메서드의 파라미터를 매핑합니다.
    // - String itemDetail: 사용자가 입력한 문자열 값을 매핑하여 쿼리에서 사용합니다.
    //
    // 반환 타입
    // - List<Item>: "Item" 엔티티의 리스트를 반환합니다. 즉, 조건에 맞는 여러 개의 Item 객체를 결과로 반환합니다.



    // 네이티브 SQL 쿼리를 정의하는 @Query 어노테이션
    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    // 메서드는 "item_detail" 컬럼에 주어진 문자열을 포함하는 항목들을 내림차순으로 조회하는 쿼리를 실행합니다.
    // value: 실제 데이터베이스 SQL 쿼리를 정의합니다.
    // nativeQuery = true: 이 쿼리가 네이티브 SQL임을 나타냅니다. 즉, JPQL이 아닌 데이터베이스에 직접 실행될 SQL입니다.
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
    // 메서드 설명:
    // - @Param("itemDetail"): JPQL 쿼리에서 사용하는 파라미터 `:itemDetail`과 메서드의 파라미터를 매핑합니다.
    // - List<Item>: 조건에 맞는 모든 `Item` 객체들을 리스트 형태로 반환합니다.
    // - findByItemDetailByNative: 주어진 `itemDetail`을 포함하는 항목들을 조회하는 네이티브 SQL 쿼리를 실행합니다.

}
