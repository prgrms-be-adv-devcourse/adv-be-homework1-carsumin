package com.example.homework.payment.domain;

/**
 * 결제 상태를 표현하는 Enum.
 *
 * - READY: 결제 정보가 생성되었으나 아직 승인되지 않은 상태
 * - CONFIRMED: Toss 결제 승인까지 완료된 상태
 * - FAILED: 결제 승인 실패 또는 오류로 인해 결제가 완료되지 못한 상태
 */
public enum PaymentStatus {
    READY,
    CONFIRMED,
    FAILED
}
