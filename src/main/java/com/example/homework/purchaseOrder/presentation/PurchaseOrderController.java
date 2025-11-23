package com.example.homework.purchaseOrder.presentation;

import com.example.homework.common.ResponseEntity;
import com.example.homework.purchaseOrder.application.dto.PurchaseOrderInfo;
import com.example.homework.purchaseOrder.domain.PurchaseOrderStatus;
import com.example.homework.purchaseOrder.application.PurchaseOrderService;
import com.example.homework.purchaseOrder.presentation.dto.PurchaseOrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PurchaseOrder 관련 API 요청을 처리하는 REST 컨트롤러.
 *
 * 주요 API:
 * - POST   /order          : 주문 생성
 * - GET    /order          : 주문 목록 조회(페이징)
 * - PATCH  /order/{id}/status : 주문 상태 변경
 */
@RestController
@RequestMapping("${api.v1}/order")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    /**
     * 주문 생성 API.
     * - 요청 DTO(PurchaseOrderRequest)를 Command로 변환하여 서비스에 전달한다.
     *
     * @param request 클라이언트가 보낸 주문 생성 요청 데이터
     * @return 생성된 주문 정보(PurchaseOrderInfo)
     */
    @Operation(summary = "주문 생성", description = "상품과 구매자 정보를 바탕으로 주문을 생성한다.")
    @PostMapping
    public ResponseEntity<PurchaseOrderInfo> create(@RequestBody PurchaseOrderRequest request) {
        return purchaseOrderService.create(request.toCommand());
    }

    /**
     * 주문 목록 조회 API.
     * - 페이징 정보를 이용해 주문 리스트를 조회한다.
     *
     * @param pageable 페이징 정보
     * @return 주문 목록(PurchaseOrderInfo 리스트)
     */
    @Operation(summary = "주문 목록 조회", description = "생성된 주문을 페이지 단위로 조회한다.")
    @GetMapping
    public ResponseEntity<List<PurchaseOrderInfo>> findAll(Pageable pageable) {
        return purchaseOrderService.findAll(pageable);
    }

    /**
     * 주문 상태 변경 API.
     * - 특정 주문의 상태를 변경한다.
     * - status 파라미터는 enum(PurchaseOrderStatus) 형태로 전달됨.
     *
     * @param id 주문 ID
     * @param status 변경할 주문 상태(CREATED, PAID, CANCELLED)
     * @return 변경된 주문 정보(PurchaseOrderInfo)
     */
    @Operation(summary = "주문 상태 변경", description = "주문의 상태를 변경한다.")
    @PatchMapping("{id}/status")
    public ResponseEntity<PurchaseOrderInfo> changeStatus(@PathVariable String id, @RequestParam PurchaseOrderStatus status) {
        return purchaseOrderService.statusChange(id, status);
    }
}
