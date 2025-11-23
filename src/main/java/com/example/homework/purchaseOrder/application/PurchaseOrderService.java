package com.example.homework.purchaseOrder.application;

import com.example.homework.purchaseOrder.application.dto.PurchaseOrderCommand;
import com.example.homework.purchaseOrder.application.dto.PurchaseOrderInfo;
import com.example.homework.purchaseOrder.domain.PurchaseOrder;
import com.example.homework.purchaseOrder.domain.PurchaseOrderStatus;
import com.example.homework.common.ResponseEntity;
import com.example.homework.purchaseOrder.infrastructure.PurchaseOrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * PurchaseOrder 도메인의 비즈니스 로직 담당하는 서비스.
 *
 * 담당역할
 * - 주문 생성
 * - 주문 목록 조회 (페이징)
 * - 주문 상태 변경 (Created → Paid → Cancelled 등)
 */
@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderJpaRepository purchaseOrderJpaRepository;

    /**
     * 새로운 주문을 생성한다.
     * - UUID 자동 생성
     * - 상태 기본값 CREATED 설정
     * - 저장 후 응답 DTO로 변환
     */
    public ResponseEntity<PurchaseOrderInfo> create(PurchaseOrderCommand command) {
        PurchaseOrder purchaseOrder = PurchaseOrder.create(
                command.productId(),
                command.sellerId(),
                command.memberId(),
                command.amount()
        );

        // 저장
        PurchaseOrder saved = purchaseOrderJpaRepository.save(purchaseOrder);
        return new ResponseEntity<>(HttpStatus.CREATED.value(), PurchaseOrderInfo.from(saved), 1);
    }

    /**
     * 주문 목록을 페이지 단위로 조회한다.
     */
    public ResponseEntity<List<PurchaseOrderInfo>> findAll(Pageable pageable) {

        // Page 조회
        Page<PurchaseOrder> purchaseOrderPage = purchaseOrderJpaRepository.findAll(pageable);

        List<PurchaseOrderInfo> purchaseOrders = purchaseOrderPage
                .map(PurchaseOrderInfo::from)
                .getContent();

        return new ResponseEntity<>(HttpStatus.OK.value(), purchaseOrders, purchaseOrders.size());
    }

    /**
     * 주문 상태를 변경한다.
     * - 존재하지 않는 주문이면 IllegalArgumentException 발생
     * - 상태 변경 후 저장
     */
    public ResponseEntity<PurchaseOrderInfo> statusChange(String id, PurchaseOrderStatus status) {

        PurchaseOrder purchaseOrder = purchaseOrderJpaRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("order not found id : " + id));

        purchaseOrder.changeStatus(status);

        PurchaseOrder updated = purchaseOrderJpaRepository.save(purchaseOrder);

        return new ResponseEntity<>(HttpStatus.OK.value(), PurchaseOrderInfo.from(updated), 1);
    }
}
