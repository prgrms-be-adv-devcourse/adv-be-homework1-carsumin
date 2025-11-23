package com.example.homework.purchaseOrder.application.dto;

import com.example.homework.purchaseOrder.domain.PurchaseOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 주문 생성, 조회 시 서비스 계층이 외부로 반환하는 결과 데이터
 * @param id : 주문번호
 * @param productId : 주문한 상품 아이디
 * @param amount : 주문 금액
 * @param status : 주문  (CREATED, PAID, CANCELLED)
 * @param createdAt : 주문 생성일
 */
public record PurchaseOrderInfo (
    UUID id,
    UUID productId,
    BigDecimal amount,
    String status,
    LocalDateTime createdAt
){
    public static PurchaseOrderInfo from(PurchaseOrder order){
        return new PurchaseOrderInfo(
                order.getId(),
                order.getProductId(),
                order.getAmount(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }
}
