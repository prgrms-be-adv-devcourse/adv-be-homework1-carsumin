package com.example.homework.purchaseOrder.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * PurchaseOrder 도메인의 Repository 인터페이스.
 */
public interface PurchaseOrderRepository {

    /**
     * 주문 목록을 페이지 단위로 조회한다.
     *
     * @param pageable 페이징 정보
     * @return 조회된 주문 목록(Page)
     */
    Page<PurchaseOrder> findAll(Pageable pageable);

    /**
     * 주문 ID로 단일 주문을 조회한다.
     *
     * @param id 주문 UUID
     * @return 주문(Optional) – 없으면 빈 값
     */
    Optional<PurchaseOrder> findById(UUID id);

    /**
     * 주문을 저장하거나 업데이트한다.
     *
     * @param purchaseOrder 저장할 엔티티
     * @return 저장된 엔티티
     */
    PurchaseOrder save(PurchaseOrder purchaseOrder);

    /**
     * 주문을 삭제한다.
     *
     * @param id 삭제할 주문의 UUID
     */
    void deleteById(UUID id);
}
