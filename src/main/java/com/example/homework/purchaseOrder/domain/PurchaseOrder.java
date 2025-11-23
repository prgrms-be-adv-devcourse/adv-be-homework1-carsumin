package com.example.homework.purchaseOrder.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PurchaseOrder 엔티티
 *
 * 이 클래스는 주문(purchase_order) 테이블과 매핑되는 도메인 모델이다.
 */
@Getter
@Entity
@Table(name = "\"purchase_order\"", schema = "public")
public class PurchaseOrder {

    @Schema(description = "주문 번호")
    @Id
    private UUID id;

    @Schema(description = "상품 id")
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Schema(description = "판매자")
    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @Schema(description = "구매자")
    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @Schema(description = "주문 가격")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Schema(description = "주문의 현재 상태 (예: CREATED, PAID, CANCELED 등)")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PurchaseOrderStatus status;

    @Schema(description = "주문 생성일")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Schema(description = "주문 수정일")
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 주문 생성 팩토리 메서드.
     *
     * 새로운 주문을 생성할 때 필요한 필수 값들을 설정하고,
     * 주문 상태를 기본값(CREATED)으로 초기화한다.
     *
     * @param productId 주문한 상품 ID
     * @param sellerId 판매자 ID
     * @param memberId 구매자 ID
     * @param amount 주문 금액
     * @return 초기화된 PurchaseOrder 엔티티
     */
    public static PurchaseOrder create(
        UUID productId,
        UUID sellerId,
        UUID memberId,
        BigDecimal amount
    ){
        PurchaseOrder order = new PurchaseOrder();
        order.productId = productId;
        order.sellerId = sellerId;
        order.memberId = memberId;
        order.amount = amount;
        order.status = PurchaseOrderStatus.CREATED; // 주문 생성 시 기본 상태
        return order;
    }

    /**
     * 주문을 '결제 완료(PAID)' 상태로 전환한다.
     * 결제가 정상적으로 승인된 뒤에만 호출해야 한다.
     */
    public void markPaid() {
        this.status = PurchaseOrderStatus.PAID;
    }

    /**
     * 주문의 상태를 변경한다.
     * @param status
     */
    public void changeStatus(PurchaseOrderStatus status){
        this.status = status;
    }

    /**
     * 엔티티가 처음 저장될 때 자동 실행됨.
     * - id가 없으면 UUID 자동 생성
     * - createdAt / updatedAt 현재 시간으로 초기화
     * - status가 null이면 CREATED로 기본값 설정
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
            status = PurchaseOrderStatus.CREATED;
        }
    }

    /**
     * 엔티티가 업데이트될 때 자동 실행됨.
     * - updatedAt 값을 현재 시간으로 갱신
     */
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
