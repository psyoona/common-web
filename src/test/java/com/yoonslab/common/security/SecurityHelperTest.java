package com.yoonslab.common.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SecurityHelperTest {

    private static final String KEY       = "my-secret-key-32bytes!!_padding!!";
    private static final String PLAINTEXT = "안녕하세요, 민감한 데이터입니다.";

    @Test
    @DisplayName("암호화 결과는 평문과 다르며 Base64 형식이다")
    void encryptProducesDifferentValue() {
        String cipher = SecurityHelper.encrypt(PLAINTEXT, KEY);
        assertThat(cipher).isNotEqualTo(PLAINTEXT);
        assertThat(cipher).matches("^[A-Za-z0-9+/=]+$");
    }

    @Test
    @DisplayName("암호화 후 복호화하면 원래 평문과 동일하다")
    void encryptThenDecryptReturnsOriginal() {
        String cipher = SecurityHelper.encrypt(PLAINTEXT, KEY);
        String decrypted = SecurityHelper.decrypt(cipher, KEY);
        assertThat(decrypted).isEqualTo(PLAINTEXT);
    }

    @Test
    @DisplayName("다른 키로 복호화하면 예외가 발생한다")
    void decryptWithWrongKeyThrows() {
        String cipher = SecurityHelper.encrypt(PLAINTEXT, KEY);
        assertThatThrownBy(() -> SecurityHelper.decrypt(cipher, "wrong-key-32bytes!!_padding_xxx!"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("복호화 실패");
    }

    @Test
    @DisplayName("키 길이가 32바이트 미만이어도 동작한다 (0x00 패딩)")
    void shortKeyWorks() {
        String shortKey = "short";
        String cipher = SecurityHelper.encrypt("hello", shortKey);
        assertThat(SecurityHelper.decrypt(cipher, shortKey)).isEqualTo("hello");
    }

    @Test
    @DisplayName("키 길이가 32바이트 초과여도 동작한다 (앞 32바이트만 사용)")
    void longKeyWorks() {
        String longKey = "this-is-a-very-long-key-that-exceeds-32-bytes!!!!!";
        String cipher = SecurityHelper.encrypt("hello", longKey);
        assertThat(SecurityHelper.decrypt(cipher, longKey)).isEqualTo("hello");
    }

    @Test
    @DisplayName("동일한 평문과 키는 항상 동일한 암호문을 생성한다")
    void deterministicEncryption() {
        String cipher1 = SecurityHelper.encrypt(PLAINTEXT, KEY);
        String cipher2 = SecurityHelper.encrypt(PLAINTEXT, KEY);
        assertThat(cipher1).isEqualTo(cipher2);
    }
}
