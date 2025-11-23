package com.example.homework.payment.infrastructure;

import com.example.homework.payment.domain.PaymentFailure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * PaymentFailure 엔티티에 대한 Spring Data JPA Repository.
 */
public interface PaymentFailureJpaRepository extends JpaRepository<PaymentFailure, UUID> {
}
