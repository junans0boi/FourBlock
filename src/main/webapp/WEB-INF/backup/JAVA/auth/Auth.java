// 컴퓨터시스템공학과 2-B 202345047 정지원
package org.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.db.DatabaseManager;

public class Auth {
    public static void main(String[] args) {  // TEST
        // 회원가입
        int registerStatus = register("1234", "1234", "testtest", "user@example.com");
        System.out.println("Registration Status: " + registerStatus);

        // 로그인
        User user = login("1234", "1234");
        if (user != null) {
            System.out.println("Login Success: " + user.getUsername());
        } else {
            System.out.println("Login Failed");
        }
    }

    public static User login(String userId, String password) {
        String salt = null;
        String storedHashedPassword = null;
        User user = null;

        // 데이터베이스 연결 및 사용자 정보 조회
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                salt = rs.getString("salt");
                storedHashedPassword = rs.getString("password");

                // 비밀번호 해싱 및 비교
                PasswordHashResult result = PasswordHasher.hashPassword(password, salt);
                if (storedHashedPassword.equals(result.getHashedPassword())) {
                    // User 객체 생성 및 정보 저장
                    int userIdInt = rs.getInt("user_id");
                    String username = rs.getString("username");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String createdAt = rs.getString("created_at");

                    user = new User(userIdInt, username, name, email, createdAt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static int register(String username, String password, String name, String email) {
        // 데이터베이스 연결 및 중복 확인
        try (Connection conn = DatabaseManager.getConnection()) {
            // 중복 확인 쿼리
            String checkQuery = "SELECT * FROM users WHERE username = ? OR email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return 1; // 이미 존재하는 사용자 이름 또는 이메일
            }

            // 솔트 생성 및 비밀번호 해싱
            PasswordHashResult hashResult = PasswordHasher.hashPassword(password);

            // 회원가입 쿼리
            String insertQuery = "INSERT INTO users (username, password, salt, name, email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, username);
            insertStmt.setString(2, hashResult.getHashedPassword());
            insertStmt.setString(3, hashResult.getSalt());
            insertStmt.setString(4, name);
            insertStmt.setString(5, email);

            int rowsAffected = insertStmt.executeUpdate();
            return rowsAffected > 0 ? 0 : -1; // 정상 처리 또는 비정상 처리
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 비정상 처리 (예외 발생)
        }
    }
}
