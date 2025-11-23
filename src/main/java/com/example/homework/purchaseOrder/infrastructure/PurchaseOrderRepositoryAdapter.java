package com.example.homework.purchaseOrder.infrastructure;

import com.example.homework.purchaseOrder.domain.PurchaseOrder;
import com.example.homework.purchaseOrder.domain.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * PurchaseOrderRepository의 JPA 기반 구현체(Adapter).
 */
@Repository
@RequiredArgsConstructor
public class PurchaseOrderRepositoryAdapter implements PurchaseOrderRepository {

    private final PurchaseOrderJpaRepository purchaseOrderJpaRepository;

    /** 주문 목록 전체 조회 (Paging) */
    @Override
    public Page<PurchaseOrder> findAll(Pageable pageable){
        return purchaseOrderJpaRepository.findAll(pageable);
    }

    /** 주문 ID 기반 조회 */
    @Override
    public Optional<PurchaseOrder> findById(UUID id){
        return purchaseOrderJpaRepository.findById(id);
    }

    /** 주문 저장 또는 수정 */
    @Override
    public PurchaseOrder save(PurchaseOrder purchaseOrder){
        return purchaseOrderJpaRepository.save(purchaseOrder);
    }

    /** 주문 삭제 */
    @Override
    public void deleteById(UUID id){
        purchaseOrderJpaRepository.deleteById(id);
    }
}
