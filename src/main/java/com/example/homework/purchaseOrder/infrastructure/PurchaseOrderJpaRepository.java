package com.example.homework.purchaseOrder.infrastructure;

import com.example.homework.purchaseOrder.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * PurchaseOrder 엔티티를 DB와 연동하는 Spring Data JPA Repository.
 */
@Repository
public interface PurchaseOrderJpaRepository extends JpaRepository<PurchaseOrder, UUID> {

}
