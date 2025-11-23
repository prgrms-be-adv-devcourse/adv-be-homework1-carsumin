package com.example.homework.payment.client;

import com.example.homework.payment.application.dto.PaymentCommand;
import com.example.homework.payment.client.dto.TossPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * TossPaymentClient
 *
 * 외부 PG(Toss Payments)의 결제 승인 API를 호출하는 클라이언트 컴포넌트.
 *
 * 주요 역할:
 * - Toss confirm API 호출 (결제 승인)
 * - 요청 헤더 및 Basic 인증 생성
 * - 승인 실패 시 상세 오류 메시지를 포함한 IllegalStateException 발생
 *
 * 서비스 계층(PaymentService)은 이 클라이언트를 통해서만
 * Toss 서버로 결제 승인 요청을 보낸다.
 */
@Component
@RequiredArgsConstructor
public class TossPaymentClient {

    private static final String CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";
    private final RestTemplate restTemplate;

    @Value("${payment.toss.secret-key}")
    private String secretKey;

    /**
     * 결제 승인 요청 메서드.
     *
     * 1. SecretKey 검증
     * 2. Toss confirm API 요청 헤더 생성
     * 3. 결제 승인 요청 본문(body) 구성
     * 4. RestTemplate으로 POST 요청
     *
     * Toss 서버에서 오류 응답이 오면 HttpStatusCodeException이 발생하고,
     * 이를 IllegalStateException으로 감싸서 던진다.
     *
     * @param command paymentKey/orderId/amount 정보를 담은 결제 승인 명령 DTO
     * @return Toss 결제 승인 응답
     */
    public TossPaymentResponse confirm(PaymentCommand command){

        // SecretKey가 없으면 승인 요청 자체가 불가능함
        if(secretKey == null) throw new IllegalStateException("Toss secret key is not configured");

        // 헤더 생성 (Basic Auth 포함)
        HttpHeaders headers = createHeaders();

        // 요청 바디 생성
        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", command.paymentKey());
        body.put("orderId", command.orderId());
        body.put("amount", command.amount());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Toss confirm API 호출
        try {
            return restTemplate.postForObject(CONFIRM_URL, entity, TossPaymentResponse.class);
        } catch (HttpStatusCodeException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            String responseBody = ex.getResponseBodyAsString();
            throw new IllegalStateException("Toss confirm failed (" + statusCode + "): " + responseBody, ex);
        }
    }

    /**
     * Toss API 요청에 필요한 헤더 생성.
     *
     * - Content-Type: application/json
     * - Authorization: Basic {Base64(secretKey:)}
     *
     * Toss는 SecretKey + ":" 형태를 Base64 인코딩한 값을 Basic 인증으로 사용한다.
     *
     * @return Header 객체
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Basic 인증 문자열 생성 (secretKey:)
        String auth = secretKey + ":";
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encoded);

        return headers;
    }

}
