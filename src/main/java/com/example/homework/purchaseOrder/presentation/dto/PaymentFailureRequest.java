package com.example.homework.purchaseOrder.presentation.dto;

import com.example.homework.payment.application.dto.PaymentFailureCommand;

/**
 * 결제 실패 콜백을 받기 위한 요청 DTO.
 */
public record PaymentFailureRequest(
        String orderId,
        String paymentKey,
        String code,
        String message,
        Long amount,
        String rawPayload
) {
    public PaymentFailureCommand toCommand(){
        return new PaymentFailureCommand(
                orderId,
                paymentKey,
                code,
                message,
                amount,
                rawPayload
        );
    }
}
