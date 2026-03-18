package com.yoonslab.common.dto;

/**
 * 표준 API 응답 래퍼 (공용 라이브러리)
 * 모든 API 응답을 { success, message, data } 구조로 통일
 *
 * 성공: ApiResponse.ok(data)
 * 실패: ApiResponse.fail("메시지")
 *
 * @param <T> 응답 데이터 타입
 */
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /** 성공 응답 (데이터 포함) */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, null, data);
    }

    /** 성공 응답 (메시지 + 데이터) */
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /** 실패 응답 */
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public boolean isSuccess() { return success; }
    public String getMessage()  { return message; }
    public T getData()          { return data; }
}
