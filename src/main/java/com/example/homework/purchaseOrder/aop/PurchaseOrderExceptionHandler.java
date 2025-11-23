package com.example.homework.purchaseOrder.aop;

import com.example.homework.common.ResponseEntity;
import com.example.homework.purchaseOrder.presentation.PurchaseOrderController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * purchaseOrder 관련 API에서 발생하는 예외를 공통 처리하는 AOP.
 */
@Slf4j
@RestControllerAdvice(assignableTypes = PurchaseOrderController.class)
public class PurchaseOrderExceptionHandler {

    /**
     * 잘못된 요청 처리 (400)
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgument(IllegalArgumentException e){
        log.warn("[PurchaseOrder] 잘못된 요청 : {}", e.getMessage());

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST.value(), null, 0);
    }

    /**
     * 그 외 모든 예외처리 (500)
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e){
        log.error("[PurchaseOrder] 서버 내부 에러 발생", e);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, 0);
    }

}
