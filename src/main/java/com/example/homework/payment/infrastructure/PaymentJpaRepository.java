package com.example.homework.payment.infrastructure;

import com.example.homework.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Payment 엔티티에 대한 Spring Data JPA Repository.
 */
public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {
}
