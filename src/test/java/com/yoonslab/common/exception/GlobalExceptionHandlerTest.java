package com.yoonslab.common.exception;

import com.yoonslab.common.dto.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("IllegalArgumentException → 400 Bad Request")
    void illegalArgument() {
        ResponseEntity<ApiResponse<Void>> res =
                handler.handleBadRequest(new IllegalArgumentException("잘못된 파라미터"));

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody().isSuccess()).isFalse();
        assertThat(res.getBody().getMessage()).isEqualTo("잘못된 파라미터");
    }

    @Test
    @DisplayName("NoSuchElementException → 404 Not Found")
    void noSuchElement() {
        ResponseEntity<ApiResponse<Void>> res =
                handler.handleNotFound(new NoSuchElementException("리소스 없음"));

        assertThat(res.getStatusCode().value()).isEqualTo(404);
        assertThat(res.getBody().isSuccess()).isFalse();
        assertThat(res.getBody().getMessage()).isEqualTo("리소스 없음");
    }
}
