package com.example.homework.payment.aop;

import com.example.homework.common.ResponseEntity;
import com.example.homework.payment.presentation.PaymentController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * PaymentController에서 발생하는 예외를 공통적으로 처리하기 위한 핸들러.
 */
@Slf4j
@RestControllerAdvice(assignableTypes = PaymentController.class)
public class PaymentExceptionHandler {

    /**
     * 주로 TossPaymentClient에서 발생하는 결제 승인 실패 상황 처리
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException e) {
        log.error("[Payment IllegalStateException] {}", e.getMessage());

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST.value(),null,0);
    }

    /**
     * 그 외 예상하지 못한 결제 관련 오류 처리
     * @param e
     * @return
     */
    // 그 외 payment 모듈에서 터지는 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("[Payment Exception] {}", e.getMessage());

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,0);
    }
}
