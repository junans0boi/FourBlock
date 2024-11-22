// 컴퓨터시스템공학과 2-B 202345047 정지원
package org.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {

    // TEST
    public static void main(String[] args) {
        String password = "mySecurePassword";
        PasswordHashResult result = hashPassword(password);

        System.out.println("Hashed Password: " + result.getHashedPassword());
        System.out.println("Salt: " + result.getSalt());
    }

    // 비밀번호 해싱 함수 (솔트값 명시 X)
    public static PasswordHashResult hashPassword(String password) {
        String salt = generateSalt();
        return hashPassword(password, salt);
    }

    // 비밀번호 해싱 함수 (솔트값 명시 호출)
    public static PasswordHashResult hashPassword(String password, String salt) {
        try {
            // SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes());
            byte[] hashedBytes = digest.digest(password.getBytes());

            // 해싱 결과를 Base64 인코딩
            String hashedPassword = Base64.getEncoder().encodeToString(hashedBytes);

            return new PasswordHashResult(hashedPassword, salt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // 난수 생성기로 솔트 값 생성
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
}
