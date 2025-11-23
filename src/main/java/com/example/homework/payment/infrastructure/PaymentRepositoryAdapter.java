package com.example.homework.payment.infrastructure;

import com.example.homework.payment.domain.Payment;
import com.example.homework.payment.domain.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * PaymentRepositoryAdapter
 *
 * 도메인 계층의 PaymentRepository 인터페이스를 구현하는 인프라 어댑터.
 */
@Repository
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Page<Payment> findAll(Pageable pageable){
        return paymentJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Payment> findById(UUID id){
        return paymentJpaRepository.findById(id);
    }

    @Override
    public Payment save(Payment payment){
        return paymentJpaRepository.save(payment);
    }
}
