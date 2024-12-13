package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
// 테스트 메서드 실행 후, 트랜잭션을 자동으로 롤백합니다.
@Transactional
class OrderTest {

    @Autowired
    OrderRepository orderRepository; // Order 엔티티에 대한 CRUD 작업을 수행하는 Repository

    @Autowired
    ItemRepository itemRepository; // Item 엔티티에 대한 CRUD 작업을 수행하는 Repository

    @PersistenceContext
    EntityManager entityManager; // JPA의 영속성 컨텍스트를 관리하는 엔티티 매니저

    /**
     * 새로운 Item(상품) 엔티티를 생성하는 메서드
     * @return Item 객체
     */
    public Item createItem(){

        Item item = new Item();
        item.setItemNm("테스트 상품"); // 상품 이름 설정
        item.setPrice(10000); // 상품 가격 설정
        item.setItemDetail("상세설명"); // 상품 상세 설명 설정
        item.setItemSellStatus(ItemSellStatus.SELL); // 상품의 판매 상태 설정 (판매 중)
        item.setStockNumber(100); // 상품의 재고 수량 설정
        item.setRegTime(LocalDateTime.now()); // 상품 등록 시간 설정
        item.setUpdateTime(LocalDateTime.now()); // 상품 수정 시간 설정

        return item; // 생성한 Item 객체 반환
    }

    /**
     * 영속성 전이(Cascade) 테스트
     * Order 엔티티를 저장할 때 연관된 OrderItem 엔티티들도 자동으로 영속화되는지 확인합니다.
     */
    @Test
    @DisplayName("영속성 전이(cascade)")
    public void cascadeTest(){

        // 새로운 주문(Order) 엔티티 생성
        Order order = new Order();

        // 3개의 OrderItem을 생성하여 Order에 추가합니다.
        for(int i = 0; i < 3; i++) {
            // 새로운 Item(상품) 생성
            Item item = this.createItem();

            // Item을 데이터베이스에 저장
            itemRepository.save(item); // Item 엔티티는 별도로 저장해야 함 (영속성 전이와는 무관)

            // 새로운 OrderItem(주문 항목) 생성
            OrderItem orderItem = new OrderItem();

            orderItem.setItem(item); // OrderItem에 연결된 상품(Item) 설정
            orderItem.setCount(10); // 주문 수량 설정
            orderItem.setOrderPrice(1000); // 주문 가격 설정
            orderItem.setOrder(order); // OrderItem이 소속된 Order 설정 (연관 관계 설정)

            // Order의 orderItems 리스트에 OrderItem 추가
            order.getOrderItems().add(orderItem);
        }

        // Order 엔티티를 저장하면서 연관된 OrderItem 엔티티도 함께 저장됨 (CascadeType.ALL 덕분에)
        orderRepository.saveAndFlush(order); // saveAndFlush: 데이터를 즉시 데이터베이스에 반영하고 영속성 컨텍스트를 비우지 않음
        entityManager.clear(); // 영속성 컨텍스트를 초기화하여, DB에서 새로 엔티티를 조회하도록 만듦

        // 데이터베이스에서 방금 저장한 Order 엔티티를 다시 조회합니다.
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new); // 만약 Order가 존재하지 않으면 예외 발생

        // Order에 포함된 OrderItem의 개수가 3개인지 확인합니다.
        assertEquals(3, savedOrder.getOrderItems().size()); // Order에 추가한 OrderItem의 개수가 3개여야 함
    }

    @Autowired
    MemberRepository memberRepository; // Member 엔티티에 대한 CRUD 작업을 수행하는 Repository

    /**
     * 새로운 Order(주문) 객체를 생성하는 메서드
     * - 3개의 OrderItem(주문 항목)을 Order에 추가합니다.
     * - Member(회원) 엔티티와 연결하여 주문을 생성합니다.
     *
     * @return 생성된 Order 객체
     */
    public Order createOrder(){

        // 1️⃣ 새로운 Order(주문) 객체 생성
        Order order = new Order();

        // 2️⃣ 3개의 OrderItem(주문 항목)을 Order에 추가
        for(int i = 0;i < 3;i++){
            // 새로운 Item(상품) 생성
            Item item = createItem(); // 상품 생성 메서드 호출

            itemRepository.save(item); // Item을 데이터베이스에 저장 (item 테이블에 INSERT 실행)

            // 새로운 OrderItem(주문 항목) 생성
            OrderItem orderItem = new OrderItem();

            orderItem.setItem(item); // OrderItem에 연결된 Item 설정
            orderItem.setCount(10); // 주문 수량 설정
            orderItem.setOrderPrice(1000); // 주문 가격 설정
            orderItem.setOrder(order); // OrderItem과 Order 간의 연관 관계 매핑

            // Order의 orderItems 리스트에 OrderItem 추가
            order.getOrderItems().add(orderItem);
        }

        // 3️⃣ 새로운 Member(회원) 생성 및 저장
        Member member = new Member();
        memberRepository.save(member); // Member 테이블에 새로운 회원을 INSERT

        // 4️⃣ Order에 Member(회원) 연결
        order.setMember(member); // 주문에 회원 정보 추가
        orderRepository.save(order); // Order와 연관된 OrderItem도 함께 저장 (CascadeType.ALL 덕분에 order_item에도 INSERT)

        return order; // 생성한 Order 객체 반환
    }

    /**
     * 고아 객체 제거 테스트 메서드
     * - Order의 orderItems 리스트에서 OrderItem을 제거합니다.
     * - orphanRemoval = true에 의해 고아 객체가 자동으로 삭제됩니다.
     */
    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        // 1️⃣ 새로운 Order(주문) 생성 (3개의 OrderItem 포함)
        Order order = this.createOrder();// 3개의 OrderItem이 포함된 Order가 생성됨

        // 2️⃣ Order의 orderItems 컬렉션에서 첫 번째 OrderItem을 제거
        order.getOrderItems().remove(0); // 리스트에서 첫 번째 OrderItem을 삭제

        // 3️⃣ 엔티티 매니저의 변경 사항을 데이터베이스에 반영
        entityManager.flush();
        // Order와 OrderItem의 변경 사항을 데이터베이스에 반영
        // **orphanRemoval = true**에 의해 OrderItem이 고아 객체로 인식되어
        // **DELETE 쿼리가 자동 실행**됩니다.
    }

    @Autowired
    OrderItemRepository orderItemRepository; // OrderItem 엔티티에 대한 CRUD 작업을 수행하는 Repository

    /**
     * **지연 로딩(Lazy Loading) 테스트 메서드**
     * - Order와 OrderItem의 관계에서 **Lazy Loading**의 동작을 확인합니다.
     * - OrderItem을 조회했을 때 **Order가 프록시 객체로 로드되는지 확인**합니다.
     */
    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest(){

        // 1️⃣ 새로운 Order(주문) 생성 (3개의 OrderItem이 포함됨)
        Order order = this.createOrder(); // 3개의 OrderItem이 포함된 Order를 생성하고 저장

        // 2️⃣ 첫 번째 OrderItem의 ID를 가져옴 (지연 로딩을 확인할 대상)
        Long orderItemId = order.getOrderItems().get(0).getId();
        // 생성된 Order의 orderItems 리스트에서 첫 번째 OrderItem의 ID를 가져옵니다.

        // 3️⃣ 영속성 컨텍스트 초기화
        entityManager.flush(); // 영속성 컨텍스트의 변경 사항을 DB에 반영합니다.
        entityManager.clear(); // **1차 캐시 초기화** (영속성 컨텍스트에 저장된 엔티티가 모두 분리됨)
        // 이를 통해 이후의 조회 작업이 1차 캐시가 아닌 **데이터베이스로부터 데이터를 다시 로드**하도록 만듭니다.

        // 4️⃣ OrderItem 조회 (OrderItem만 로드됨, Order는 지연 로딩)
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        // **OrderItem만 조회**합니다.
        // OrderItem과 연관된 Order는 **Lazy Loading**으로 설정되어 있으므로, **Order 객체는 로드되지 않고 프록시로 유지**됩니다.

        // 5️⃣ OrderItem의 연관된 Order의 클래스 정보 출력
        System.out.println("Order class : " + orderItem.getOrder().getClass());
        // **orderItem.getOrder()**는 Lazy Loading으로 인해 **프록시 객체(HibernateProxy)로 로드**됩니다.
        // 프록시 객체의 실제 클래스 이름이 출력되며, Hibernate에서는 **Order$$EnhancerByHibernate...**와 같은 이름으로 표시됩니다.

        System.out.println("=================================================================");
        orderItem.getOrder().getOrderDate();
        System.out.println("=================================================================");
    }
}