package com.example.homework.common;

import lombok.Data;

/**
 * API 공통 응답 모델.
 *
 *  - status : HTTP 상태 코드
 *  - data   : 실제 응답 데이터(payload)
 *  - count  : 데이터 개수
 */
@Data
public class ResponseEntity<T> {
    private final int status;
    private final T data;
    private final long count;

    public ResponseEntity(int value, T all, long count) {
        this.status = value;
        this.data = all;
        this.count = count;
    }
}
