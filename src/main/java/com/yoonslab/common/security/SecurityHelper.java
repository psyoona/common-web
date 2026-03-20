package com.yoonslab.common.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * AES-256 암복호화 유틸리티 (공용 라이브러리)
 *
 * - 알고리즘 : AES-256 / CBC / PKCS5Padding
 * - 암호키   : 전달받은 키를 UTF-8 바이트로 변환 후 32바이트로 맞춤 (부족 시 0x00 패딩, 초과 시 잘라냄)
 * - IV      : 암호키의 앞 16자리(바이트) 사용
 * - 출력    : Base64 인코딩 문자열
 *
 * 사용 예:
 *   String cipher = SecurityHelper.encrypt("민감한 데이터", "my-secret-key-32bytes!!");
 *   String plain  = SecurityHelper.decrypt(cipher, "my-secret-key-32bytes!!");
 */
public class SecurityHelper {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int KEY_LENGTH   = 32; // AES-256 = 32바이트
    private static final int IV_LENGTH    = 16; // CBC IV = 16바이트

    private SecurityHelper() {}

    /**
     * AES-256 암호화
     *
     * @param plaintext 평문
     * @param secretKey 암호키 (길이 무관, 내부적으로 32바이트로 조정)
     * @return Base64 인코딩된 암호문
     */
    public static String encrypt(String plaintext, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec(secretKey), ivSpec(secretKey));
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new IllegalStateException("암호화 실패", e);
        }
    }

    /**
     * AES-256 복호화
     *
     * @param ciphertext Base64 인코딩된 암호문
     * @param secretKey  암호화 시 사용한 암호키
     * @return 복호화된 평문
     */
    public static String decrypt(String ciphertext, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec(secretKey), ivSpec(secretKey));
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("복호화 실패", e);
        }
    }

    /** 키를 32바이트로 맞춰 SecretKeySpec 생성 */
    private static SecretKeySpec keySpec(String secretKey) {
        byte[] keyBytes = Arrays.copyOf(
                secretKey.getBytes(StandardCharsets.UTF_8), KEY_LENGTH);
        return new SecretKeySpec(keyBytes, "AES");
    }

    /** 키의 앞 16바이트를 IV로 사용 */
    private static IvParameterSpec ivSpec(String secretKey) {
        byte[] ivBytes = Arrays.copyOf(
                secretKey.getBytes(StandardCharsets.UTF_8), IV_LENGTH);
        return new IvParameterSpec(ivBytes);
    }
}
