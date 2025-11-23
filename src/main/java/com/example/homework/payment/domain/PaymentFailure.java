package com.example.homework.payment.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PaymentFailure 엔티티
 *
 * 이 클래스는 결제 실패 이력(payment_failure) 테이블과 매핑되는 도메인 모델이다.
 * Toss 결제 승인 실패 또는 요청 오류 등 결제 실패 상황을 저장하기 위해 사용한다.
 */
@Getter
@Entity
@Table(name = "\"payment_failure\"", schema = "public")
public class PaymentFailure {
    @Id
    @Schema(description = "실패 로그 고유 식별자")
    private UUID id;

    @Column(name = "order_id", nullable = false, length = 100)
    @Schema(description = "실패가 발생한 주문 ID")
    private String orderId;

    @Column(name = "payment_key", length = 200)
    @Schema(description = "실패한 결제 키")
    private String paymentKey;

    @Column(name = "error_code", length = 50)
    @Schema(description = "PG사에서 전달한 오류 코드")
    private String errorCode;

    @Column(name = "error_message")
    @Schema(description = "PG사에서 전달한 오류 메시지")
    private String errorMessage;

    @Column(name = "amount")
    @Schema(description = "실패 시점의 결제 금액")
    private Long amount;

    @Lob
    @Column(name = "raw_payload", columnDefinition="TEXT")
    @Schema(description = "토스에서 받은 전체 실패 응답 전문")
    private String rawPayload;

    @Column(name = "created_at", nullable = false)
    @Schema(description = "실패 기록 생성 시각")
    private LocalDateTime createdAt;

    protected PaymentFailure() {
    }

    /**
     * 결제 실패 정보를 생성하는 private 생성자.
     *
     * PaymentFailure는 외부 생성자가 아닌 from() 팩토리 메서드를 통해서만 생성된다.
     *
     * @param orderId 결제 실패가 발생한 주문 번호
     * @param paymentKey 결제키(존재할 수도 있고 없을 수도 있음)
     * @param errorCode Toss 또는 시스템에서 전달된 실패 코드
     * @param errorMessage 실패 상세 메시지
     * @param amount 실패 시점의 결제 금액
     * @param rawPayload Toss 응답 전문(JSON 문자열)
     */
    private PaymentFailure(
            String orderId,
            String paymentKey,
            String errorCode,
            String errorMessage,
            Long amount,
            String rawPayload
    ) {
        this.id = UUID.randomUUID();
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.amount = amount;
        this.rawPayload = rawPayload;
    }

    /**
     * PaymentFailure 생성 팩토리 메서드.
     *
     * 서비스 레이어에서는 이 메서드를 통해서만 PaymentFailure를 생성한다.
     */
    public static PaymentFailure from(
            String orderId,
            String paymentKey,
            String errorCode,
            String errorMessage,
            Long amount,
            String rawPayload
    ) {
        return new PaymentFailure(
                orderId,
                paymentKey,
                errorCode,
                errorMessage,
                amount,
                rawPayload
        );
    }

    /**
     * 엔티티가 처음 저장될 때 실행됨.
     * - id가 없으면 UUID 자동 생성
     * - createdAt 현재 시간으로 자동 세팅
     */
    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        createdAt = LocalDateTime.now();
    }
}
