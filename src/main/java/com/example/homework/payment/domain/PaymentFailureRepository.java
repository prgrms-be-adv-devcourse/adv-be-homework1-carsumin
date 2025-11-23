package com.example.homework.payment.domain;

/**
 * PaymentFailure 엔티티를 저장하기 위한 도메인 레벨 Repository 인터페이스.
 */
public interface PaymentFailureRepository {
    PaymentFailure save(PaymentFailure failure);
}
