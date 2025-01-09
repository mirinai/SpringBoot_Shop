package com.shop.repository;

import com.shop.entity.ItemImg; // ItemImg 엔티티를 import
import org.springframework.data.jpa.repository.JpaRepository; // JpaRepository 인터페이스를 import

import java.util.List;

/**
 * 📘 **ItemImgRepository 인터페이스**
 *
 * 이 인터페이스는 **상품 이미지(ItemImg) 테이블에 접근하기 위한 데이터 접근 객체(DAO) 역할**을 합니다.
 *
 * 🛠️ **주요 역할**
 * - **JPA를 통해 ItemImg 엔티티에 대한 CRUD 기능을 자동으로 제공**합니다.
 * - **JpaRepository<ItemImg, Long>**을 상속받아, 기본적인 데이터베이스 접근 메서드(CRUD)를 사용할 수 있습니다.
 * - JpaRepository는 스프링 데이터 JPA가 제공하는 인터페이스로, **findAll(), save(), delete(), findById()** 같은 메서드를 사용할 수 있습니다.
 */
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    ItemImg findByItemIdAndRepImgYn(Long itemId, String repImgYn);
}

