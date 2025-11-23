package com.example.homework.payment.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 결제(Payment) 엔티티.
 *
 * Toss 결제 승인 결과를 저장하고 관리하기 위한 도메인 모델
 */
@Getter
@Entity
@Table(name = "\"payment\"", schema = "public")
public class Payment {

    @Id
    @Schema(description = "결제 고유 식별자")
    private UUID id;

    @Column(name = "payment_key", nullable = false, unique = true, length = 200)
    @Schema(description = "토스에서 발급한 결제 키")
    private String paymentKey;

    @Column(name = "order_id", nullable = false, length = 100)
    @Schema(description = "결제가 연동된 주문 ID")
    private String orderId;

    @Column(name = "total_amount", nullable = false)
    @Schema(description = "결제 총액")
    private Long amount;

    @Column(name = "method", length = 50)
    @Schema(description = "결제 수단 (카드, 간편결제 등)")
    private String method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "결제 상태 (READY, CONFIRMED, FAILED 등)")
    private PaymentStatus status;

    @Column(name = "requested_at")
    @Schema(description = "토스 측에 결제 승인 요청을 보낸 시각")
    private LocalDateTime requestedAt;

    @Column(name = "approved_at")
    @Schema(description = "토스에서 결제가 승인된 시각")
    private LocalDateTime approvedAt;

    @Column(name = "fail_reason")
    @Schema(description = "결제 실패 사유")
    private String failReason;

    @Column(name = "created_at", nullable = false)
    @Schema(description = "엔티티 생성 시각")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @Schema(description = "엔티티 최종 수정 시각")
    private LocalDateTime updatedAt;

    protected Payment() {
    }

    /**
     * 결제 생성용 private 생성자.
     *
     * 결제는 최초 생성 시 READY 상태를 가진다.
     * 아직 Toss 승인 전이며 단순히 결제 정보가 준비된 단계임.
     *
     * @param paymentKey 토스에서 전달되는 결제 식별 키
     * @param orderId 주문 번호
     * @param amount 결제 금액
     */
    private Payment(String paymentKey, String orderId, Long amount) {
        this.id = UUID.randomUUID();
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
        this.status = PaymentStatus.READY; // 처음 생성될 때 상태는 READY (아직 토스 승인 완료 상태가 아님, 승인 전에 생성만 하는 개념)
    }

    /**
     * Payment 엔티티 생성 팩토리 메서드.
     *
     * 외부에서 Payment 인스턴스를 만들 때 생성자가 아닌 이 메서드를 사용함.
     *
     * @param paymentKey 결제키
     * @param orderId 주문번호
     * @param amount 결제 금액
     * @return 초기화된 Payment 객체
     */
    public static Payment create(
            String paymentKey,
            String orderId,
            Long amount
    ) {
        return new Payment(paymentKey, orderId, amount);
    }

    /**
     * 결제를 '승인 완료(CONFIRMED)' 상태로 전환한다.
     *
     * Toss 결제 승인 성공 후 서비스에서 호출된다.
     * 승인 시간, 요청 시간, 결제수단 등의 추가 정보를 함께 저장한다.
     *
     * @param method 결제 수단 (카드/간편결제 등)
     * @param approvedAt 승인 완료 시간
     * @param requestedAt 결제 요청 시간
     */
    public void markConfirmed(
            String method,
            LocalDateTime approvedAt,
            LocalDateTime requestedAt
    ) {
        this.status = PaymentStatus.CONFIRMED; // 토스에서 결제 승인 성공했을 때 상태
        this.method = method; // 카드, 간편결제 등
        this.approvedAt = approvedAt; // 승인시간
        this.requestedAt = requestedAt; // 요청시간
        this.failReason = null;
    }

    /**
     * 결제를 '실패(FAILED)' 상태로 전환한다.
     *
     * 결제 승인 실패 또는 Toss 응답 오류 등이 발생했을 때 호출하여
     * 실패 사유를 기록한다.
     *
     * @param failReason 실패 사유 메시지
     */
    public void markFailed(
            String failReason
    ) {
        this.status = PaymentStatus.FAILED;
        this.failReason = failReason;
    }

    /**
     * 엔티티가 처음 저장될 때 실행됨.
     *
     * - id가 없으면 UUID 자동 생성
     * - createdAt / updatedAt 현재 시각으로 초기화
     * - status가 비어있으면 READY로 기본 설정
     */
    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
        createdAt = now;
        updatedAt = now;
        if (status == null) {
            status = PaymentStatus.READY;
        }
    }

    /**
     * 엔티티 업데이트 시 실행됨.
     * - updatedAt 값을 현재 시간으로 갱신
     */
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
