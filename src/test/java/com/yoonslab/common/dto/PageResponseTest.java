package com.yoonslab.common.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResponseTest {

    @Test
    @DisplayName("전체 25개, 페이지 크기 10 → totalPages=3")
    void totalPages() {
        PageResponse<String> res = new PageResponse<>(List.of(), 1, 10, 25L);
        assertThat(res.getTotalPages()).isEqualTo(3);
    }

    @Test
    @DisplayName("1페이지 → startPage=1, endPage=3, hasPrevious=false")
    void firstBlock() {
        PageResponse<String> res = new PageResponse<>(List.of(), 1, 10, 25L);
        assertThat(res.getStartPage()).isEqualTo(1);
        assertThat(res.getEndPage()).isEqualTo(3);
        assertThat(res.isHasPrevious()).isFalse();
        assertThat(res.isHasNext()).isFalse();
    }

    @Test
    @DisplayName("11페이지 → startPage=11, hasPrevious=true")
    void secondBlock() {
        // 총 150개, 페이지 크기 10 → totalPages=15
        PageResponse<String> res = new PageResponse<>(List.of(), 11, 10, 150L);
        assertThat(res.getStartPage()).isEqualTo(11);
        assertThat(res.getEndPage()).isEqualTo(15);
        assertThat(res.isHasPrevious()).isTrue();
        assertThat(res.isHasNext()).isFalse();
    }

    @Test
    @DisplayName("데이터 없을 때 → totalPages=0, hasNext=false")
    void empty() {
        PageResponse<String> res = new PageResponse<>(List.of(), 1, 10, 0L);
        assertThat(res.getTotalPages()).isEqualTo(0);
        assertThat(res.isHasNext()).isFalse();
    }
}
