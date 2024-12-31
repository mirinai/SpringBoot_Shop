package com.shop.exception;

// RuntimeException을 상속받아 OutOfStockException을 정의함.
// 이 예외는 재고 부족 상황에서 발생하도록 설계됨.
public class OutOfStockException extends RuntimeException {

    // OutOfStockException 생성자
    // 부모 클래스 RuntimeException의 생성자를 호출하며, 예외 메시지를 전달받음.
    public OutOfStockException(String message) {
        super(message); // 전달받은 메시지를 RuntimeException에 전달
    }
}