package com.yoonslab.common;

import com.yoonslab.common.dto.CursorPageResponse;
import com.yoonslab.common.dto.PageResponse;

import java.util.List;

/**
 * 라이브러리 동작 확인용 콘솔 실행 클래스
 * 실행: ./gradlew runDemo
 */
public class Demo {

    public static void main(String[] args) {
        pageResponseDemo();
        System.out.println("test..");
        cursorPageResponseDemo();
    }

    static void pageResponseDemo() {
        System.out.println("=== PageResponse ===");

        // 총 25개 데이터, 페이지 크기 10, 현재 1페이지
        PageResponse<String> page1 = new PageResponse<>(
                List.of("item1", "item2", "item3"), 1, 10, 25L);

        System.out.println("totalPages   : " + page1.getTotalPages());   // 3
        System.out.println("startPage    : " + page1.getStartPage());    // 1
        System.out.println("endPage      : " + page1.getEndPage());      // 3
        System.out.println("hasPrevious  : " + page1.isHasPrevious());   // false
        System.out.println("hasNext      : " + page1.isHasNext());       // false

        System.out.println("--- 11페이지 (총 150개) ---");
        PageResponse<String> page11 = new PageResponse<>(List.of(), 11, 10, 150L);
        System.out.println("startPage    : " + page11.getStartPage());   // 11
        System.out.println("endPage      : " + page11.getEndPage());     // 15
        System.out.println("hasPrevious  : " + page11.isHasPrevious());  // true
        System.out.println("hasNext      : " + page11.isHasNext());      // false
    }

    static void cursorPageResponseDemo() {
        System.out.println("=== CursorPageResponse ===");

        CursorPageResponse<String> res = CursorPageResponse.<String>builder()
                .content(List.of("a", "b", "c"))
                .nextCursor(42L)
                .hasNext(true)
                .size(3)
                .totalElements(10L)
                .build();

        System.out.println("content      : " + res.getContent());
        System.out.println("nextCursor   : " + res.getNextCursor());     // 42
        System.out.println("hasNext      : " + res.isHasNext());         // true
        System.out.println("totalElements: " + res.getTotalElements());  // 10
    }
}
