package com.example.homework.purchaseOrder.domain;

/**
 * PurchaseOrder의 상태를 나타내는 도메인 enum.
 *
 * 상태 설명:
 * - CREATED   : 주문 생성 완료 (기본 상태)
 * - PAID      : 결제 완료
 * - CANCELLED : 주문 취소됨
 */
public enum PurchaseOrderStatus {
    CREATED,
    PAID,
    CANCELLED
}
