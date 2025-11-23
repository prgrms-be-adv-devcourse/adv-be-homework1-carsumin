package com.example.homework.purchaseOrder.presentation.dto;

import com.example.homework.purchaseOrder.application.dto.PurchaseOrderCommand;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 주문 API에서 사용되는 요청 DTO.
 */
public record PurchaseOrderRequest(
    String productId,
    String sellerId,
    String memberId,
    BigDecimal amount
) {
    public PurchaseOrderCommand toCommand(){
        return new PurchaseOrderCommand(
                UUID.fromString(productId),
                UUID.fromString(sellerId),
                UUID.fromString(memberId),
                amount
        );
    }
}
