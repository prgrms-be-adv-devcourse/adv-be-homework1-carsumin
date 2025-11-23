package com.example.homework.purchaseOrder.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 주문 생성 시 필요한 입력 데이터를 전달하기 위한 Command 객체.
 * @param productId : 주문할 상품 아이디
 * @param sellerId : 판매자 아이디
 * @param memberId : 구매자 아이디
 * @param amount : 주문 금액
 */
public record PurchaseOrderCommand (
    UUID productId,
    UUID sellerId,
    UUID memberId,
    BigDecimal amount
){

}

