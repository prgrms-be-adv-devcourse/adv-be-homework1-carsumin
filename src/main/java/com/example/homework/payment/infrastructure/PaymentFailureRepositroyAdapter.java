package com.example.homework.payment.infrastructure;

import com.example.homework.payment.domain.PaymentFailure;
import com.example.homework.payment.domain.PaymentFailureRepository;
import org.springframework.stereotype.Repository;

/**
 * PaymentFailureRepositoryAdapter
 *
 * 도메인 계층(PaymentFailureRepository)과 JPA 구현체(PaymentFailureJpaRepository)를
 * 연결하는 어댑터 역할을 수행한다.
 */
@Repository
public class PaymentFailureRepositroyAdapter implements PaymentFailureRepository {
    private final PaymentFailureJpaRepository paymentFailureJpaRepository;

    public PaymentFailureRepositroyAdapter(PaymentFailureJpaRepository paymentFailureJpaRepository){
        this.paymentFailureJpaRepository = paymentFailureJpaRepository;
    }

    @Override
    public PaymentFailure save(PaymentFailure failure){
        return paymentFailureJpaRepository.save(failure);
    }
}
