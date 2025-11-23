package com.example.homework.payment.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Payment 엔티티의 조회/저장을 위한 도메인 레벨 Repository 인터페이스.
 */
public interface PaymentRepository {

    /**
     * 결제 목록을 페이지 형태로 조회한다.
     */
    Page<Payment> findAll(Pageable pageable);

    /**
     * 결제 ID로 단일 결제를 조회한다.
     */
    Optional<Payment> findById(UUID id);

    /**
     * 결제 엔티티를 저장하거나 업데이트한다.
     */
    Payment save(Payment payment);
}
