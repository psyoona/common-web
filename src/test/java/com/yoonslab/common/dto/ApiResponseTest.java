package com.yoonslab.common.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    @DisplayName("ok(data) - success=true, message=null, data 존재")
    void okWithData() {
        ApiResponse<String> res = ApiResponse.ok("hello");
        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getMessage()).isNull();
        assertThat(res.getData()).isEqualTo("hello");
    }

    @Test
    @DisplayName("ok(message, data) - success=true, message와 data 모두 존재")
    void okWithMessageAndData() {
        ApiResponse<Integer> res = ApiResponse.ok("조회 성공", 42);
        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getMessage()).isEqualTo("조회 성공");
        assertThat(res.getData()).isEqualTo(42);
    }

    @Test
    @DisplayName("fail(message) - success=false, data=null")
    void fail() {
        ApiResponse<Void> res = ApiResponse.fail("존재하지 않는 리소스");
        assertThat(res.isSuccess()).isFalse();
        assertThat(res.getMessage()).isEqualTo("존재하지 않는 리소스");
        assertThat(res.getData()).isNull();
    }
}
