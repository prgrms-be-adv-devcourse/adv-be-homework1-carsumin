package com.example.homework.payment.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * Toss 결제 승인 응답 DTO.
 *
 * 토스 결제 승인 API(/v1/payments/confirm) 호출 결과를 매핑하기 위한 데이터 구조이다.
 * - paymentKey: 결제 건을 식별하는 고유 키
 * - orderId: 결제 시 사용한 주문 번호
 * - totalAmount: 승인된 결제 금액
 * - method: 결제 방식(카드/간편결제 등)
 * - status: 토스 결제 상태 문자열
 * - requestedAt: 결제 요청 시간
 * - approvedAt: 결제 승인 완료 시간
 *
 * 토스 API의 응답 필드 중, 사용하지 않는 필드는 자동으로 무시한다(@JsonIgnoreProperties).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TossPaymentResponse(
        String paymentKey,
        String orderId,

        @JsonProperty("totalAmount")
        Long totalAmount,

        String method,
        String status,

        OffsetDateTime requestedAt,
        OffsetDateTime approvedAt
) {
}
