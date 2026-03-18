package com.yoonslab.common.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CursorPageResponseTest {

    @Test
    @DisplayName("Builder로 생성 - 다음 페이지 있음")
    void builderWithNext() {
        CursorPageResponse<String> res = CursorPageResponse.<String>builder()
                .content(List.of("a", "b", "c"))
                .nextCursor(42L)
                .hasNext(true)
                .size(3)
                .totalElements(10L)
                .build();

        assertThat(res.getContent()).containsExactly("a", "b", "c");
        assertThat(res.getNextCursor()).isEqualTo(42L);
        assertThat(res.isHasNext()).isTrue();
        assertThat(res.getSize()).isEqualTo(3);
        assertThat(res.getTotalElements()).isEqualTo(10L);
    }

    @Test
    @DisplayName("마지막 페이지 - hasNext=false, nextCursor=null")
    void lastPage() {
        CursorPageResponse<String> res = CursorPageResponse.<String>builder()
                .content(List.of("x"))
                .nextCursor(null)
                .hasNext(false)
                .size(10)
                .totalElements(1L)
                .build();

        assertThat(res.isHasNext()).isFalse();
        assertThat(res.getNextCursor()).isNull();
    }

    @Test
    @DisplayName("생성자로 직접 생성")
    void constructorCreation() {
        CursorPageResponse<Integer> res = new CursorPageResponse<>(
                List.of(1, 2, 3), 99L, true, 3, 100L);

        assertThat(res.getContent()).hasSize(3);
        assertThat(res.getNextCursor()).isEqualTo(99L);
    }
}
