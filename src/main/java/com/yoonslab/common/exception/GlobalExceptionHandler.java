package com.yoonslab.common.exception;

import com.yoonslab.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * 전역 예외 핸들러 (공용 라이브러리)
 * - 클라이언트 오류(4xx)를 올바른 HTTP 상태로 반환
 * - 내부 예외 메시지가 500으로 그대로 노출되는 것을 방지
 *
 * 사용법: app 모듈의 @SpringBootApplication에
 *   scanBasePackages = {"com.example.demo", "com.yoonslab.common"} 추가
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 잘못된 파라미터 (status 열거형 오류, 범위 초과 등) → 400 */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
    }

    /** 존재하지 않는 리소스 → 404 */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoSuchElementException e) {
        return ResponseEntity.status(404).body(ApiResponse.fail(e.getMessage()));
    }
}
