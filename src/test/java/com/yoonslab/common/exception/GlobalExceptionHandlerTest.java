package com.yoonslab.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("IllegalArgumentException → 400 Bad Request")
    void illegalArgument() {
        ResponseEntity<Map<String, String>> res =
                handler.handleBadRequest(new IllegalArgumentException("잘못된 파라미터"));

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).containsEntry("error", "잘못된 파라미터");
    }

    @Test
    @DisplayName("NoSuchElementException → 404 Not Found")
    void noSuchElement() {
        ResponseEntity<Map<String, String>> res =
                handler.handleNotFound(new NoSuchElementException("리소스 없음"));

        assertThat(res.getStatusCode().value()).isEqualTo(404);
        assertThat(res.getBody()).containsEntry("error", "리소스 없음");
    }
}
