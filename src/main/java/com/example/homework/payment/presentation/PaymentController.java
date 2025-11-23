package com.example.homework.payment.presentation;

import com.example.homework.common.ResponseEntity;
import com.example.homework.payment.application.PaymentService;
import com.example.homework.payment.application.dto.PaymentFailureInfo;
import com.example.homework.payment.application.dto.PaymentInfo;
import com.example.homework.payment.presentation.dto.PaymentRequest;
import com.example.homework.purchaseOrder.presentation.dto.PaymentFailureRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PaymentController
 *
 * 결제와 관련된 API 엔드포인트를 제공하는 프레젠테이션 계층.
 *
 * 주요 기능:
 * - 결제 목록 조회
 * - 토스 결제 승인 처리(confirm)
 * - 결제 실패 정보 저장(fail)
 *
 * 클라이언트(Rest 요청) → 서비스 계층 → 도메인 계층 흐름의 진입점 역할을 한다.
 */
@RestController
@RequestMapping("${api.v1}/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 내역 조회 API
     *
     * - 페이징 정보를 기반으로 결제 리스트를 조회한다.
     * - Payment 엔티티를 PaymentInfo DTO로 변환하여 반환한다.
     *
     * @param pageable 페이징 정보(page, size 등)
     * @return 결제 정보 목록
     */
    @Operation(summary = "결제 내역 조회", description = "확정된 결제 정보를 페이지 단위로 조회한다.")
    @GetMapping
    public ResponseEntity<List<PaymentInfo>> findAll(Pageable pageable){
        return paymentService.findAll(pageable);
    }

    /**
     * 토스 결제 승인 API
     *
     * 결제 위젯에서 결제가 완료되면 프론트는 paymentKey/orderId/amount를 서버에 전달한다.
     * 서버는 해당 값들을 기반으로 토스 confirm API를 호출하여 결제를 최종 승인한다.
     *
     * 성공 시:
     *  - 승인된 결제 정보(PaymentInfo)를 반환
     * 실패 시:
     *  - PaymentExceptionHandler(AOP)에서 오류를 공통 처리
     *
     * @param request paymentKey, orderId, amount를 포함한 요청 DTO
     * @return 승인 완료된 결제 정보
     */
    @Operation(summary = "토스 결제 승인", description = "토스 결제 완료 후 paymentKey/orderId/amount를 전달받아 결제를 승인한다.")
    @PostMapping("/confirm")
    public ResponseEntity<PaymentInfo> confirm(@RequestBody PaymentRequest request){
        return paymentService.confirm(request.toCommand());
    }

    /**
     * 결제 실패 기록 API
     *
     * - 토스 API 호출 실패, 승인 오류 발생 시 프론트에서 이 API를 호출한다.
     * - 실패 코드, 메시지, 원본 응답(rawPayload)을 PaymentFailure 엔티티로 저장한다.
     * - 저장된 실패 이력은 장애 분석 및 이슈 파악에 활용된다.
     *
     * @param request 결제 실패 정보(errorCode, errorMessage, paymentKey 등)
     * @return 저장된 실패 이력 정보(PaymentFailureInfo)
     */
    @Operation(summary = "결제 실패 기록", description = "토스 결제 실패 정보를 저장한다.")
    @PostMapping("/fail")
    public ResponseEntity<PaymentFailureInfo> fail(@RequestBody PaymentFailureRequest request){

        return paymentService.recordFailure(request.toCommand());
    }
}
