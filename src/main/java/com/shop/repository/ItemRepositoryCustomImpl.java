package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.PatternUtils;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    // Querydsl에서 제공하는 객체로, JPQL(Java Persistence Query Language)을 대신해 타입 안전한 쿼리를 생성하고 실행하는 데 사용
    private JPAQueryFactory queryFactory;// 동적으로 쿼리를 생성하기 위해 JPAQueryFactory 클래스를 씀

    // JPAQueryFactory 생성자: EntityManager를 받아 Querydsl 쿼리를 실행하는 객체 생성
    public ItemRepositoryCustomImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // 판매 상태 조건 생성: 판매 상태가 null이 아니면 해당 상태와 일치하는 조건 생성
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    // 등록 날짜 조건 생성: 검색 유형(searchDateType)에 따라 등록일 기준 조건 생성
    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        // 'all'이거나 null이면 조건 없음 (전체 검색)
        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        }
        // '1d': 최근 하루, '1w': 최근 일주일, '1m': 최근 한 달, '6m': 최근 6개월
        else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        }
        else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        }
        else if (StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }
        else if(StringUtils.equals("6m",searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        // regTime(등록 시간)이 dateTime 이후인 조건 반환
        return QItem.item.regTime.after(dateTime);


    }

    // 검색 조건 생성: 검색 유형(searchBy)과 검색어(searchQuery)에 따라 조건 생성
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        // 상품명 기준 검색: 검색어가 상품명에 포함된 조건
        if (StringUtils.equals("itemNm", searchBy)) {
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        }
        // 등록자 기준 검색: 검색어가 등록자 아이디에 포함된 조건
        else if (StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        // 조건이 없을 경우 null 반환
        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        //        QueryResults<Item> results = queryFactory
        //                .selectFrom(QItem.item)
        //                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
        //                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
        //                        searchByLike(itemSearchDto.getSearchBy(),itemSearchDto.getSearchQuery()))
        //                .orderBy(QItem.item.id.desc())
        //                .offset(pageable.getOffset())
        //                .limit(pageable.getPageSize())
        //                .fetchResults();
        //        List<Item> content = results.getResults();
        //        long total = results.getTotal();
        //        return new PageImpl<>(content,pageable,total);

        // 데이터 가져오기
        // 데이터 조회 쿼리
        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(
                        regDtsAfter(itemSearchDto.getSearchDateType()), // 등록일 기준 조건
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()), // 판매 상태 조건
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()) // 검색 조건 (상품명 또는 등록자 기준)
                )
                .orderBy(QItem.item.id.desc()) // 최신 데이터 순으로 정렬
                .offset(pageable.getOffset()) // 페이징 시작 위치
                .limit(pageable.getPageSize()) // 페이징 크기
                .fetch(); // 결과 데이터 리스트 가져오기

        //        // 총 개수 계산
        //        long total = queryFactory
        //                .select(QItem.item.id)
        //                .from(QItem.item)
        //                .where(
        //                        regDtsAfter(itemSearchDto.getSearchDateType()),
        //                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
        //                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
        //                )
        //                .fetch()
        //                .size(); // 리스트의 크기를 활용하여 총 개수 계산

        // count 전용 쿼리
        // 총 데이터 수 계산 쿼리
        Long total = queryFactory
                .select(QItem.item.count()) // 데이터 개수만 가져오는 쿼리
                .from(QItem.item)
                .where(
                        regDtsAfter(itemSearchDto.getSearchDateType()), // 등록일 기준 조건
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()), // 판매 상태 조건
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()) // 검색 조건 (상품명 또는 등록자 기준)
                )
                .fetchOne(); // 총 데이터 개수 반환

        // Page 객체 반환
        //        return new PageImpl<>(content,pageable,total);
        return new PageImpl<>(content, pageable, total != null ? total : 0); // 데이터와 페이징 정보 포함하여 반환
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){

        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

//        QueryResults<MainItemDto> results = queryFactory
//                .select(
//                        new QMainItemDto(
//                                item.id,
//                                item.itemNm,
//                                item.itemDetail,
//                                item.itemDetail,
//                                itemImg.imgUrl,
//                                item.price
//                        )
//                )
//                .from(itemImg)
//                .join(itemImg.item, item)
//                .where(itemImg.repImgYn.eq("Y"))
//                .where(itemNmLike(itemSearchDto.getSearchQuery()))
//                .orderBy(item.id.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//
//        List<MainItemDto> content = results.getResults();
//        long total = results.getTotal();
//        return new PageImpl<>(content, pageable, total);
        // 데이터 리스트 조회
        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price
                        )
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 조회
        Long totalCount = queryFactory
                .select(item.count())
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        // null 체크 후 기본값 처리
        long total = (totalCount != null) ? totalCount : 0;

        // Page 객체 반환
        return new PageImpl<>(content, pageable, total);

    }
}
