package com.example.homework.payment.presentation.dto;

import com.example.homework.payment.application.dto.PaymentCommand;

/**
 * 토스 결제 완료 후 프론트에서 전달하는 파라미터.
 */
public record PaymentRequest(
    String paymentKey,
    String orderId,
    Long amount
) {
    public PaymentCommand toCommand(){
        return new PaymentCommand(paymentKey, orderId, amount);
    }
}
