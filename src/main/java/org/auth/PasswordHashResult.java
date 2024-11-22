// 컴퓨터시스템공학과 2-B 202345047 정지원
package org.auth;

// 해싱된 비밀번호와 솔트 값을 저장하는 클래스
class PasswordHashResult {
    private final String hashedPassword;
    private final String salt;

    public PasswordHashResult(String hashedPassword, String salt) {
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getSalt() {
        return salt;
    }
}