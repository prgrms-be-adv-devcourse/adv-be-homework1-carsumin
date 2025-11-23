package com.example.homework.payment.application;

import com.example.homework.common.ResponseEntity;
import com.example.homework.payment.application.dto.PaymentCommand;
import com.example.homework.payment.application.dto.PaymentFailureCommand;
import com.example.homework.payment.application.dto.PaymentFailureInfo;
import com.example.homework.payment.application.dto.PaymentInfo;
import com.example.homework.payment.client.TossPaymentClient;
import com.example.homework.payment.client.dto.TossPaymentResponse;
import com.example.homework.payment.domain.Payment;
import com.example.homework.payment.domain.PaymentFailure;
import com.example.homework.payment.domain.PaymentFailureRepository;
import com.example.homework.payment.domain.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PaymentService
 *
 * 결제 처리와 관련된 핵심 비즈니스 로직을 담당하는 서비스 계층.
 *
 * 주요 역할:
 * - 결제 승인(Confirm) 처리
 * - 결제 목록 조회
 * - 결제 실패 이력 저장
 *
 * 외부 PG(Toss) 연동과 도메인 엔티티 생성/상태 전환 등을 모두 이 서비스에서 관리한다.
 */
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentFailureRepository paymentFailureRepository;
    private final TossPaymentClient tossPaymentClient;

    /**
     * 결제 목록 조회
     *
     * Pageable 조건에 따라 결제 목록을 조회하고 PaymentInfo 형태로 변환해 반환한다.
     *
     * @param pageable 페이징 정보
     * @return 결제 목록 및 전체 개수를 담은 ResponseEntity
     */
    public ResponseEntity<List<PaymentInfo>> findAll(Pageable pageable){

        Page<Payment> page = paymentRepository.findAll(pageable);

        List<PaymentInfo> payments = page
                .map(PaymentInfo::from)
                .getContent();

        return new ResponseEntity<>(HttpStatus.OK.value(), payments, page.getTotalElements());
    }

    /**
     * 결제 승인(Confirm)
     *
     * 1. TossPaymentClient를 통해 토스 결제 승인 API를 호출한다.
     * 2. 응답(PaymentKey, Amount, Method 등)을 기반으로 Payment 엔티티를 생성한다.
     * 3. 승인 시간(approvedAt), 요청 시간(requestedAt)을 엔티티에 반영한다.
     * 4. 승인 상태(CONFIRMED)로 변경 후 DB에 저장한다.
     *
     * @param command 결제 승인에 필요한 파라미터(paymentKey, orderId, amount)
     * @return 승인된 결제 정보
     */
    public ResponseEntity<PaymentInfo> confirm(PaymentCommand command){
        // 1) 토스에 결제 승인을 요청
        TossPaymentResponse tossPayment = tossPaymentClient.confirm(command);

        // 2) 승인 기반 Payment 엔티티 생성
        Payment payment = Payment.create(
                tossPayment.paymentKey(),
                tossPayment.orderId(),
                tossPayment.totalAmount()
        );

        // 3) 응답 시간값 변환
        LocalDateTime approvedAt = tossPayment.approvedAt() != null ? tossPayment.approvedAt().toLocalDateTime() : null;
        LocalDateTime requestedAt = tossPayment.requestedAt() != null ? tossPayment.requestedAt().toLocalDateTime() : null;

        // 4) 결제 승인 상태로 전환
        payment.markConfirmed(tossPayment.method(), approvedAt, requestedAt);

        // 5) DB 저장
        Payment saved = paymentRepository.save(payment);

        return new ResponseEntity<>(HttpStatus.CREATED.value(), PaymentInfo.from(saved), 1);
    }

    /**
     * 결제 실패 이력 저장
     *
     * Toss 실패 응답(code, message, rawPayload)을 그대로 PaymentFailure 엔티티에 저장한다.
     * 결제 실패에 대한 원본 로그를 남겨 분석 및 장애 대응에 활용한다.
     *
     * @param command 결제 실패 정보
     * @return 저장된 실패 이력 정보
     */
    public ResponseEntity<PaymentFailureInfo> recordFailure(PaymentFailureCommand command){
        PaymentFailure failure = PaymentFailure.from(
                command.orederId(),
                command.paymentKey(),
                command.errorCode(),
                command.errorMessage(),
                command.amount(),
                command.rawPayload()
        );
        PaymentFailure saved = paymentFailureRepository.save(failure);

        return new ResponseEntity<>(HttpStatus.OK.value(), PaymentFailureInfo.from(saved), 1);
    }

}
